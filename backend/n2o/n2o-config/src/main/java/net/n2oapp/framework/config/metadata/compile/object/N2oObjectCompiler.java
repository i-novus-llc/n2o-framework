package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActions;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция объекта
 */
@Component
public class N2oObjectCompiler<C extends ObjectContext> implements BaseSourceCompiler<CompiledObject, N2oObject, C> {

    @Override
    public Class<N2oObject> getSourceClass() {
        return N2oObject.class;
    }

    @Override
    public CompiledObject compile(N2oObject source, C context, CompileProcessor p) {
        CompiledObject compiled = new CompiledObject();
        compiled.setId(source.getId());
        compiled.setTableName(source.getTableName());
        compiled.setAppName(source.getAppName());
        compiled.setModuleName(source.getModuleName());
        compiled.setServiceName(source.getServiceName());

        compiled.setOperations(new StrictMap<>());
        compiled.setObjectFields(new ArrayList<>());
        compiled.setObjectFieldsMap(new HashMap<>());
        initObjectFields(source.getObjectFields(), compiled);
        compiled.setName(castDefault(source.getName(), source.getId()));
        compiled.setValidationsMap(new HashMap<>());
        compiled.setValidations(initValidations(source, compiled, context, p));
        compiled.setValidationsMap(initValidationsMap(compiled.getValidations()));
        initOperationsMap(source, compiled, context, p);

        if (context instanceof ActionContext) {
            ActionContext actionContext = (ActionContext) context;
            if (actionContext.getValidations() != null)
                compiled.setFieldValidations(actionContext.getValidations());
        }
        return compiled;
    }

    /**
     * Инициализация полей объекта
     *
     * @param objectFields Массив полей объекта
     * @param compiled     Скомпилированный объект
     */
    private void initObjectFields(AbstractParameter[] objectFields, CompiledObject compiled) {
        if (objectFields != null) {
            for (AbstractParameter field : objectFields) {
                compiled.getObjectFields().add(field);
                compiled.getObjectFieldsMap().put(field.getId(), field);
                if (field instanceof ObjectReferenceField)
                    initRefField((ObjectReferenceField) field, compiled);
            }
        }
    }

    /**
     * Инициализация ссылочного поля объекта
     *
     * @param field    Ссылочное поле
     * @param compiled Скомпилированный объект
     */
    @Deprecated
    private void initRefField(ObjectReferenceField field, CompiledObject compiled) {
        AbstractParameter[] referenceFields = field.getFields();
        if (referenceFields == null || referenceFields.length == 0) {
            ObjectSimpleField innerField = new ObjectSimpleField();
            innerField.setId("id");
            innerField.setMapping("id");
            field.getObjectReferenceFields().add(innerField);
            field.setNullIgnore(true);
        } else
            for (AbstractParameter objectScalarField : referenceFields)
                if (objectScalarField instanceof ObjectSimpleField)
                    field.getObjectReferenceFields().add((ObjectSimpleField) objectScalarField);
        if (compiled.getObjectReferenceFieldsMap() == null)
            compiled.setObjectReferenceFieldsMap(new HashMap<>());
        if (field.getId().contains("."))
            field.setId(field.getId().substring(field.getId().indexOf('.') + 1, field.getId().length() - 1));
        compiled.getObjectReferenceFieldsMap().put(field.getId(), field);
    }

    /**
     * Инициализация валидаций объекта
     *
     * @param source   Исходная модель объекта
     * @param compiled Скомпилированный объект
     * @param context  Контекст сборки объекта
     * @param p        Процессор сборки метаданных
     * @return Список валидаций объекта
     */
    private List<Validation> initValidations(N2oObject source, CompiledObject compiled, C context, CompileProcessor p) {
        List<Validation> result = new ArrayList<>();
        if (source.getN2oValidations() != null) {
            for (N2oValidation validation : source.getN2oValidations()) {
                if (validation instanceof N2oInvocationValidation) {
                    N2oInvocationValidation invocationValidation = (N2oInvocationValidation) validation;
                    if (invocationValidation.getInParameters() != null)
                        for (N2oObject.Parameter parameter : invocationValidation.getInParameters()) {
                            resolveDefaultParameter(parameter, compiled);
                            parameter.setRequired(p.cast(parameter.getRequired(), parameter.getDefaultValue() == null));
                        }
                    prepareOperationInvocation(invocationValidation.getN2oInvocation(), source);
                }
                result.add(p.compile(validation, context));
            }
        }
        return result;
    }

    /**
     * Инициализация Map операций объекта
     *
     * @param source   Исходная модель объекта
     * @param compiled Скомпилированный объект
     * @param context  Контекст сборки объекта
     * @param p        Процессор сборки метаданных
     */
    private void initOperationsMap(N2oObject source, CompiledObject compiled, C context, CompileProcessor p) {
        if (source.getOperations() != null) {
            for (N2oObject.Operation operation : source.getOperations()) {
                final CompiledObject.Operation compileOperation = compileOperation(operation, compiled, p);
                if (operation.getInParameters() != null)
                    for (N2oObject.Parameter parameter : operation.getInParameters()) {
                        AbstractParameter compiledParameter = compiled.getObjectFieldsMap().get(parameter.getId());
                        if (compiledParameter != null) {
                            parameter.setPluralityType(compiledParameter.getPluralityType());
                            parameter.setNullIgnore(p.cast(parameter.getNullIgnore(), compiledParameter.getNullIgnore()));
                            if (compiledParameter instanceof ObjectReferenceField)
                                initRefFieldChildParams(parameter, ((ObjectReferenceField) compiledParameter), p);
                        }
                    }
                prepareOperationInvocation(operation.getInvocation(), source);
                compiled.getOperations().put(compileOperation.getId(), compileOperation);
                initOperationValidations(operation, compileOperation, source, compiled, context, p);
            }
        }
    }

    /**
     * Подготовка провайдера данных операции исходного объекта
     *
     * @param invocation Исходная модель провайдера данных
     * @param source     Исходная модель объекта
     */
    private void prepareOperationInvocation(N2oInvocation invocation, N2oObject source) {
        if (invocation instanceof N2oJavaDataProvider) {
            N2oJavaDataProvider javaDataProvider = (N2oJavaDataProvider) invocation;
            if (javaDataProvider.getClassName() == null)
                javaDataProvider.setClassName(source.getServiceClass());
            if (source.getEntityClass() != null && javaDataProvider.getArguments() != null)
                Arrays.stream(javaDataProvider.getArguments())
                        .filter(arg -> arg.getClassName() == null && arg.getType() == Argument.Type.ENTITY)
                        .forEach(arg -> arg.setClassName(source.getEntityClass()));
        }
    }

    /**
     * Инициализация дочерних полей ссылочного поля объекта
     *
     * @param parameter         Параметр исходной модели объекта
     * @param compiledParameter Параметр скомпилированного объекта
     * @param p                 Процессор сборки метаданных
     */
    private void initRefFieldChildParams(N2oObject.Parameter parameter, ObjectReferenceField compiledParameter, CompileProcessor p) {
        List<N2oObject.Parameter> childParams = new ArrayList<>();
        for (ObjectSimpleField field : compiledParameter.getObjectReferenceFields()) {
            N2oObject.Parameter childParam = new N2oObject.Parameter();
            childParam.setId(field.getId());
            childParam.setMapping(field.getMapping());
            childParam.setNormalize(field.getNormalize());
            childParam.setDomain(field.getDomain());
            childParam.setDefaultValue(field.getDefaultValue());
            childParams.add(childParam);
        }
        parameter.setEntityClass(p.cast(parameter.getEntityClass(), compiledParameter.getEntityClass()));
        parameter.setMapping(p.cast(parameter.getMapping(), compiledParameter.getMapping()));
        parameter.setChildParams(childParams.toArray(new N2oObject.Parameter[0]));
    }

    /**
     * Инициализация валидаций операции объекта
     *
     * @param operation         Операция исходной модели объекта
     * @param compiledOperation Операция скомпилированного объекта
     * @param source            Исходная модель объекта
     * @param compiled          Скомпилированный объект
     * @param context           Контекст сборки объекта
     * @param p                 Процессор сборки метаданных
     */
    private void initOperationValidations(N2oObject.Operation operation, CompiledObject.Operation compiledOperation,
                                          N2oObject source, CompiledObject compiled, C context, CompileProcessor p) {
        if (compiledOperation.getValidations() != null) {
            if (compiledOperation.getValidations().getActivate() == null
                    && compiledOperation.getValidations().getBlackList() == null
                    && compiledOperation.getValidations().getWhiteList() == null)
                compiledOperation.getValidations().setActivate(N2oObject.Operation.Validations.Activate.all);
            else if (compiledOperation.getValidations().getBlackList() != null && compiledOperation.getValidations().getWhiteList() != null)
                throw new N2oException("Whitelist is incompatible with blacklist");
            if (compiledOperation.getValidations().getActivate() != null)
                fillValidationsByActivate(operation, compiledOperation, source);

            List<Validation> validationList = new ArrayList<>();
            List<Validation> whiteListValidationList = new ArrayList<>();

            if (compiledOperation.getValidations().getWhiteList() != null)
                whiteListValidationList = getWhiteListValidations(compiledOperation.getValidations().getWhiteList(),
                        compiled.getValidationsMap(), validationList, compiledOperation.getValidations().getActivate());
            else if (compiledOperation.getValidations().getBlackList() != null)
                validationList.addAll(getBlackListValidations(
                        compiledOperation.getValidations().getBlackList(), compiled.getValidationsMap()));

            List<Validation> inlineValidations = getInlineValidations(
                    compiledOperation.getValidations().getInlineValidations(), source, compiled, context, p);
            if (inlineValidations != null) {
                validationList.addAll(inlineValidations);
                whiteListValidationList.addAll(inlineValidations);
            }

            compiledOperation.setValidationList(validationList);
            compiledOperation.setValidationsMap(initValidationsMap(validationList));
            compiledOperation.setWhiteListValidationsMap(initValidationsMap(whiteListValidationList));
        }

        List<Validation> requiredParamValidations = getRequiredParamValidations(compiledOperation.getInParametersMap(), p);
        if (!requiredParamValidations.isEmpty()) {
            if (compiledOperation.getValidationList() == null)
                compiledOperation.setValidationList(new ArrayList<>());
            compiledOperation.getValidationList().addAll(requiredParamValidations);
        }

        if (context instanceof ActionContext && ((ActionContext) context).getValidations() != null) {
            if (compiledOperation.getValidationList() == null)
                compiledOperation.setValidationList(new ArrayList<>());
            mergeValidations(compiledOperation.getValidationList(), ((ActionContext) context).getValidations());
        }
    }

    /**
     * Получение списка white валидаций операции скомпилированного объекта
     * Полученный список white валидаций также производит дополнение списка всех валидаций операции
     * в переменной validationList
     *
     * @param whiteList      Массив идентификаторов white валидаций операции скомпилированного объекта
     * @param validationsMap Map валидаций скомпилированного объекта
     * @param validationList Список всех валидаций операции
     * @param activate       Тип активации валидаций
     * @return Список white валидаций операции объекта
     */
    private List<Validation> getWhiteListValidations(String[] whiteList, Map<String, Validation> validationsMap,
                                                     List<Validation> validationList,
                                                     N2oObject.Operation.Validations.Activate activate) {
        List<Validation> whiteListValidations = new ArrayList<>();
        for (String name : whiteList) {
            Validation validation = validationsMap.get(name.trim());
            if (!Boolean.FALSE.equals(validation.getEnabled())) {
                validationList.add(validation);
                if (activate != N2oObject.Operation.Validations.Activate.all)
                    whiteListValidations.add(validation);
            }
        }
        return whiteListValidations;
    }

    /**
     * Получение списка black валидаций операции скомпилированного объекта
     *
     * @param blackList      Массив идентификаторов black валидаций операции скомпилированного объекта
     * @param validationsMap Map валидаций скомпилированного объекта
     * @return Список black валидаций операции объекта
     */
    private List<Validation> getBlackListValidations(String[] blackList, Map<String, Validation> validationsMap) {
        Map<String, Validation> blackListValidationsMap = new HashMap<>(validationsMap);
        for (String name : blackList)
            blackListValidationsMap.remove(name.trim());
        return new ArrayList<>(blackListValidationsMap.values());
    }

    /**
     * Получение списка скомпилированных инлайн валидаций операции
     *
     * @param validations Массив инлайн валидаций операции
     * @param source      Исходная модель объекта
     * @param compiled    Скомпилированный объект
     * @param context     Контекст сборки объекта
     * @param p           Процессор сборки метаданных
     * @return Список скомпилированных инлайн валидаций операции
     */
    private List<Validation> getInlineValidations(N2oValidation[] validations, N2oObject source, CompiledObject compiled,
                                                  C context, CompileProcessor p) {
        if (validations == null)
            return null;

        List<Validation> inlineValidations = new ArrayList<>();
        for (N2oValidation n2oValidation : validations) {
            if ("false".equals(n2oValidation.getEnabled()))
                continue;
            if (n2oValidation instanceof N2oInvocationValidation) {
                N2oInvocationValidation invocationValidation = (N2oInvocationValidation) n2oValidation;
                if (invocationValidation.getInParameters() != null)
                    for (N2oObject.Parameter parameter : invocationValidation.getInParameters()) {
                        resolveDefaultParameter(parameter, compiled);
                        parameter.setRequired(p.cast(parameter.getRequired(), parameter.getDefaultValue() == null));
                    }
                prepareOperationInvocation(invocationValidation.getN2oInvocation(), source);
            }
            inlineValidations.add(p.compile(n2oValidation, context));
        }
        return inlineValidations;
    }

    /**
     * Получение списка валидаций обязательности входящих полей скомпилированного объекта
     *
     * @param inParamsMap Map входящих полей скомпилированного объекта
     * @param p           Процессор сборки метаданных
     * @return Список валидаций обязательности входящих полей скомпилированного объекта
     */
    private List<Validation> getRequiredParamValidations(Map<String, N2oObject.Parameter> inParamsMap, CompileProcessor p) {
        List<Validation> requiredParamValidations = new ArrayList<>();
        for (N2oObject.Parameter parameter : inParamsMap.values()) {
            if (parameter.getRequired() != null && parameter.getRequired()) {
                MandatoryValidation validation = new MandatoryValidation(parameter.getId(),
                        p.getMessage("n2o.required.field"), parameter.getId());
                validation.setMoment(N2oValidation.ServerMoment.beforeOperation);
                requiredParamValidations.add(validation);
            }
        }
        return requiredParamValidations;
    }

    /**
     * Слияние двух списков валидаций. В случае совпадения приоритетней считается валидация контрола
     *
     * @param operationValidations Валидации операции объекта
     * @param controlValidations   Валидации контрола
     */
    private void mergeValidations(List<Validation> operationValidations, List<Validation> controlValidations) {
        for (Validation cv : controlValidations) {
            operationValidations.removeIf(validation -> validation.getId().equals(cv.getId()));
            operationValidations.add(cv);
        }
    }

    /**
     * Заполнение white или black списка валидаций операции скомпилированного объекта
     * согласно типу активации его валидаций
     *
     * @param operation         Операция исходной модели объекта
     * @param compiledOperation Операция скомпилированного объекта
     * @param source            Исходная модель объекта
     */
    private void fillValidationsByActivate(N2oObject.Operation operation, CompiledObject.Operation compiledOperation, N2oObject source) {
        switch (compiledOperation.getValidations().getActivate()) {
            case all:
                if (compiledOperation.getValidations().getWhiteList() == null &&
                        compiledOperation.getValidations().getBlackList() == null &&
                        source.getN2oValidations() != null) {
                    String[] whiteList = Arrays.stream(source.getN2oValidations())
                            .map(N2oValidation::getId).toArray(String[]::new);
                    compiledOperation.getValidations().setWhiteList(whiteList);
                }
                break;
            case whiteList:
                String[] whiteList = Arrays.stream(operation.getValidations().getRefValidations())
                        .map(N2oObject.Operation.Validations.Validation::getRefId)
                        .toArray(String[]::new);
                compiledOperation.getValidations().setWhiteList(whiteList);
                break;
            case blackList:
                String[] blackList = Arrays.stream(operation.getValidations().getRefValidations())
                        .map(N2oObject.Operation.Validations.Validation::getRefId)
                        .toArray(String[]::new);
                compiledOperation.getValidations().setBlackList(blackList);
                break;
            case nothing:
                break;
        }
    }

    /**
     * Формирование Мap валидаций по списку валидаций,
     * где ключом является идентификатор валидации
     *
     * @param validations Список валидаций
     * @return Map валидаций
     */
    private static Map<String, Validation> initValidationsMap(List<Validation> validations) {
        return validations.stream().collect(Collectors.toMap(Validation::getId, Function.identity()));
    }

    /**
     * Компиляция операции объекта
     *
     * @param operation Операция исходного объекта
     * @param compiled  Скомпилированный объект
     * @param processor Процессор сборки метаданных
     * @return Операция скомпилированного объекта
     */
    private CompiledObject.Operation compileOperation(N2oObject.Operation operation, CompiledObject compiled, CompileProcessor processor) {
        Map<String, N2oObject.Parameter> inParamMap = prepareOperationParameters(operation.getInParameters(), compiled,
                p -> p.setRequired(castDefault(p.getRequired(), false)));
        Map<String, N2oObject.Parameter> outParamMap = prepareOperationParameters(operation.getOutParameters(), compiled, null);
        Map<String, N2oObject.Parameter> failOutParamMap = prepareOperationParameters(operation.getFailOutParameters(), compiled, null);

        CompiledObject.Operation compiledOperation = new CompiledObject.Operation(inParamMap, outParamMap);
        compiledOperation.setFailOutParametersMap(failOutParamMap);

        compileOperationProperties(operation, compiledOperation, processor);
        if (compiledOperation.getInParametersMap() != null)
            for (N2oObject.Parameter parameter : compiledOperation.getInParametersMap().values())
                resolveDefaultParameter(parameter, compiled);

        compiledOperation.setProperties((processor.mapAttributes(operation)));
        return compiledOperation;
    }

    /**
     * Компиляция свойств операции объекта
     *
     * @param operation         Операция исходной модели объекта
     * @param compiledOperation Операция скомпилированного объекта
     * @param p                 Процессор сборки метаданных
     */
    private void compileOperationProperties(N2oObject.Operation operation, CompiledObject.Operation compiledOperation, CompileProcessor p) {
        compiledOperation.setId(operation.getId());
        compiledOperation.setDescription(operation.getDescription());
        compiledOperation.setName(operation.getName());
        compiledOperation.setConfirm(operation.getConfirm());
        compiledOperation.setConfirmationText(castDefault(operation.getConfirmationText(),
                p.getMessage("n2o.confirm.text")));
        compiledOperation.setBulkConfirmationText(castDefault(operation.getBulkConfirmationText(),
                p.getMessage("n2o.confirm.group")));
        compiledOperation.setSuccessText(castDefault(operation.getSuccessText(),
                p.getMessage("n2o.success")));
        compiledOperation.setFailText(operation.getFailText());
        compiledOperation.setInvocation(operation.getInvocation());
        compiledOperation.setValidations(operation.getValidations());
        DefaultActions defaultOperations = DefaultActions.get(operation.getId());
        if (defaultOperations != null) {
            compiledOperation.setFormSubmitLabel(castDefault(operation.getFormSubmitLabel(), defaultOperations.getFormSubmitLabel()));
            compiledOperation.setName(castDefault(operation.getName(), defaultOperations.getLabel()));
        }
        compiledOperation.setFormSubmitLabel(operation.getFormSubmitLabel());
    }

    /**
     * Выполнение подготовительных действий над параметрами (in, out или fail-out) операции объекта
     * и сборка их в map
     *
     * @param parameters Параметры операции исходной модели объекта
     * @param compiled   Скомпилированный объект
     * @param consumer   Действия, выполняемые над параметром
     * @return Map параметров операции исходной модели объекта
     */
    private Map<String, N2oObject.Parameter> prepareOperationParameters(N2oObject.Parameter[] parameters, CompiledObject compiled,
                                                                        Consumer<N2oObject.Parameter> consumer) {
        Map<String, N2oObject.Parameter> params = new LinkedHashMap<>();
        if (parameters != null)
            for (N2oObject.Parameter p : parameters) {
                resolveDefaultParameter(p, compiled);
                if (consumer != null)
                    consumer.accept(p);
                params.put(p.getId(), p);
            }
        return params;
    }

    /**
     * Присвоение значений по умолчанию полям параметра, если они не были заданы
     *
     * @param parameter Параметр вызова
     * @param compiled  Скомпилированный объект
     */
    private <T extends InvocationParameter> void resolveDefaultParameter(T parameter, CompiledObject compiled) {
        AbstractParameter field = compiled.getObjectFieldsMap().get(parameter.getId());
        if (parameter.getDomain() == null)
            parameter.setDomain(resolveDefaultDomain(field));
        if (parameter.getRequired() == null)
            parameter.setRequired(resolveDefaultRequired(field));
        if (parameter.getDefaultValue() == null)
            parameter.setDefaultValue(resolveDefaultValue(field));
        if (parameter.getNormalize() == null)
            parameter.setNormalize(resolveDefaultNormalize(field));
        if (parameter.getMapping() == null)
            parameter.setMapping(resolveDefaultMapping(field));
    }

    /**
     * Получение значения по умолчанию для типа данных
     *
     * @param field Поле объекта
     * @return Значение по умолчанию для типа данных
     */
    private String resolveDefaultDomain(AbstractParameter field) {
        String domain = null;
        if (field instanceof ObjectSimpleField && ((ObjectSimpleField) field).getDomain() != null)
            domain = ((ObjectSimpleField) field).getDomain();
        return domain;
    }

    /**
     * Получение значения по умолчанию для свойства обязательности
     *
     * @param field Поле объекта
     * @return Значение по умолчанию для свойства обязательности
     */
    private Boolean resolveDefaultRequired(AbstractParameter field) {
        Boolean required = null;
        if (field != null)
            required = field.getRequired();
        return required;
    }

    /**
     * Получение значения по умолчанию
     *
     * @param field Поле объекта
     * @return Значение по умолчанию
     */
    private String resolveDefaultValue(AbstractParameter field) {
        String defaultValue = null;
        if (field instanceof ObjectSimpleField && ((ObjectSimpleField) field).getDefaultValue() != null)
            defaultValue = ((ObjectSimpleField) field).getDefaultValue();
        return defaultValue;
    }

    /**
     * Получение значения по умолчанию для нормализации
     *
     * @param field Поле объекта
     * @return Значение по умолчанию для нормализации
     */
    private String resolveDefaultNormalize(AbstractParameter field) {
        String normalizer = null;
        if (field instanceof ObjectSimpleField && ((ObjectSimpleField) field).getNormalize() != null)
            normalizer = ((ObjectSimpleField) field).getNormalize();
        return normalizer;
    }

    /**
     * Получение значения по умолчанию для маппинга
     *
     * @param field Поле объекта
     * @return Значение по умолчанию для маппинга
     */
    private String resolveDefaultMapping(AbstractParameter field) {
        String mapping = null;
        if (field != null)
            mapping = field.getMapping();
        return mapping;
    }
}
