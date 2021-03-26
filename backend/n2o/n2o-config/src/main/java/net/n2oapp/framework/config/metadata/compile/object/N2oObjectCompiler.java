package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
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
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция объекта
 */
@Component
public class N2oObjectCompiler<C extends ObjectContext> implements BaseSourceCompiler<CompiledObject, N2oObject, C> {

    private static final int OBJECT_REFERENCE_NESTING_MAX_DEPTH = 3;

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
        initObjectFields(source, compiled, p);
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
     * @param source   Исходная модель объекта
     * @param compiled Скомпилированный объект
     * @param p        Процессор сборки метаданных
     */
    private void initObjectFields(N2oObject source, CompiledObject compiled,
                                  CompileProcessor p) {
        if (source.getObjectFields() != null) {
            for (AbstractParameter field : source.getObjectFields()) {
                if (field instanceof ObjectReferenceField && ((ObjectReferenceField) field).getReferenceObjectId() != null)
                    initReferenceFieldByObjectId((ObjectReferenceField) field, p, 1);
                compiled.getObjectFields().add(field);
                compiled.getObjectFieldsMap().put(field.getId(), field);
            }
        }
    }

    /**
     * Инициализация структуры составного поля по содержимому objectId, на который он ссылается
     *
     * @param refField     Исходная модель поля, ссылающегося на другой объект
     * @param p            Процессор сборки метаданных
     * @param currentDepth Текущая глубина вложенности
     */
    private void initReferenceFieldByObjectId(ObjectReferenceField refField, CompileProcessor p, int currentDepth) {
        if (currentDepth > OBJECT_REFERENCE_NESTING_MAX_DEPTH)
            return;

        N2oObject refObject = p.getSource(refField.getReferenceObjectId(), N2oObject.class);
        refField.setEntityClass(p.cast(refField.getEntityClass(), refObject.getEntityClass()));

        if (ArrayUtils.isNotEmpty(refField.getFields())) {
            if (ArrayUtils.isNotEmpty(refObject.getObjectFields())) {
                for (int i = 0; i < refField.getFields().length; i++) {
                    AbstractParameter parameter = refField.getFields()[i];
                    Optional<AbstractParameter> objectRefField = Arrays.stream(refObject.getObjectFields())
                            .filter(f -> f.getId().equals(parameter.getId())).findFirst();
                    if (objectRefField.isPresent() && objectRefField.get().getClass().equals(parameter.getClass()))
                        refField.getFields()[i] = p.merge(objectRefField.get(), parameter);
                }
            }
        } else
            refField.setFields(refObject.getObjectFields());
        refField.setReferenceObjectId(null);

        // рекурсивная инициализация внутренних вложенных полей
        if (ArrayUtils.isNotEmpty(refField.getFields()))
            for (AbstractParameter field : refField.getFields())
                if (field instanceof ObjectReferenceField && ((ObjectReferenceField) field).getReferenceObjectId() != null)
                    initReferenceFieldByObjectId((ObjectReferenceField) field, p, currentDepth++);
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
                if (validation instanceof N2oInvocationValidation)
                    prepareInvocationValidation(source, (N2oInvocationValidation) validation, compiled, p);
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
                prepareOperationInvocation(operation.getInvocation(), source);
                compiled.getOperations().put(compileOperation.getId(), compileOperation);
                initOperationValidations(compileOperation, source, compiled, context, p);
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
     * Инициализация валидаций операции объекта
     *
     * @param compiledOperation Операция скомпилированного объекта
     * @param source            Исходная модель объекта
     * @param compiled          Скомпилированный объект
     * @param context           Контекст сборки объекта
     * @param p                 Процессор сборки метаданных
     */
    private void initOperationValidations(CompiledObject.Operation compiledOperation, N2oObject source,
                                          CompiledObject compiled, C context, CompileProcessor p) {
        if (compiledOperation.getValidations() != null) {
            boolean activateAll = false;
            if (compiledOperation.getValidations().getBlackList() == null &&
                    compiledOperation.getValidations().getWhiteList() == null &&
                    source.getN2oValidations() != null) {
                String[] whiteList = Arrays.stream(source.getN2oValidations())
                        .map(N2oValidation::getId).toArray(String[]::new);
                compiledOperation.getValidations().setWhiteList(whiteList);
                activateAll = true;
            } else if (compiledOperation.getValidations().getBlackList() != null &&
                    compiledOperation.getValidations().getWhiteList() != null)
                throw new N2oException("Whitelist is incompatible with blacklist");

            List<Validation> validationList = new ArrayList<>();
            List<Validation> whiteListValidationList = new ArrayList<>();

            if (compiledOperation.getValidations().getWhiteList() != null)
                whiteListValidationList = getWhiteListValidations(compiledOperation.getValidations().getWhiteList(),
                        compiled.getValidationsMap(), validationList, activateAll);
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
     * @param activateAll    Используются ли все валидации
     * @return Список white валидаций операции объекта
     */
    private List<Validation> getWhiteListValidations(String[] whiteList, Map<String, Validation> validationsMap,
                                                     List<Validation> validationList, boolean activateAll) {
        List<Validation> whiteListValidations = new ArrayList<>();
        for (String name : whiteList) {
            Validation validation = validationsMap.get(name.trim());
            if (!Boolean.FALSE.equals(validation.getEnabled())) {
                validationList.add(validation);
                if (activateAll)
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
            if (n2oValidation instanceof N2oInvocationValidation)
                prepareInvocationValidation(source, (N2oInvocationValidation) n2oValidation, compiled, p);
            inlineValidations.add(p.compile(n2oValidation, context));
        }
        return inlineValidations;
    }

    /**
     * Подготовка валидации с вызовом провайдера данных операции
     *
     * @param source     Исходная модель объекта
     * @param validation Валидация с вызовом провайдера данных операции
     * @param compiled   Скомпилированный объект
     * @param p          Процессор сборки метаданных
     */
    private void prepareInvocationValidation(N2oObject source, N2oInvocationValidation validation, CompiledObject compiled, CompileProcessor p) {
        if (validation.getInFields() != null)
            for (AbstractParameter parameter : validation.getInFields()) {
                AbstractParameter field = compiled.getObjectFieldsMap().get(parameter.getId());
                if (parameter instanceof ObjectSimpleField) {
                    if (field instanceof ObjectSimpleField)
                        resolveSimpleFieldDefault((ObjectSimpleField) parameter, (ObjectSimpleField) field);
                    parameter.setRequired(p.cast(parameter.getRequired(), ((ObjectSimpleField) parameter).getDefaultValue() == null));
                } else if (parameter instanceof ObjectReferenceField && field instanceof ObjectReferenceField)
                    resolveReferenceFieldDefault((ObjectReferenceField) parameter, (ObjectReferenceField) field);
            }
        prepareOperationInvocation(validation.getN2oInvocation(), source);
    }

    /**
     * Получение списка валидаций обязательности входящих полей скомпилированного объекта
     *
     * @param inParamsMap Map входящих полей скомпилированного объекта
     * @param p           Процессор сборки метаданных
     * @return Список валидаций обязательности входящих полей скомпилированного объекта
     */
    private List<Validation> getRequiredParamValidations(Map<String, AbstractParameter> inParamsMap, CompileProcessor p) {
        List<Validation> requiredParamValidations = new ArrayList<>();
        for (AbstractParameter parameter : inParamsMap.values()) {
            if (Boolean.TRUE.equals(parameter.getRequired())) {
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
     * @param p         Процессор сборки метаданных
     * @return Операция скомпилированного объекта
     */
    private CompiledObject.Operation compileOperation(N2oObject.Operation operation, CompiledObject compiled,
                                                      CompileProcessor p) {
        CompiledObject.Operation compiledOperation = new CompiledObject.Operation();
        compiledOperation.setInParametersMap(prepareOperationInParameters(operation.getInFields(), compiled, p));

        compiledOperation.setOutParametersMap(operation.getOutFields() != null ?
                Arrays.stream(operation.getOutFields()).collect(Collectors.toMap(ObjectSimpleField::getId, Function.identity())) :
                Collections.emptyMap());
        compiledOperation.setFailOutParametersMap(operation.getFailOutFields() != null ?
                Arrays.stream(operation.getFailOutFields()).collect(Collectors.toMap(ObjectSimpleField::getId, Function.identity())) :
                Collections.emptyMap());

        compileOperationProperties(operation, compiledOperation, p);
        compiledOperation.setProperties(p.mapAttributes(operation));
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
     * Выполнение подготовительных действий над входящими параметрами операции объекта и сборка их в map
     *
     * @param parameters Входящие параметры операции исходной модели объекта
     * @param compiled   Скомпилированный объект
     * @param p          Процессор сборки метаданных
     * @return Map входящих параметров операции исходной модели объекта
     */
    private Map<String, AbstractParameter> prepareOperationInParameters(AbstractParameter[] parameters,
                                                                        CompiledObject compiled, CompileProcessor p) {
        Map<String, AbstractParameter> inFieldsMap = new LinkedHashMap<>();
        if (parameters != null)
            for (AbstractParameter parameter : parameters) {
                prepareOperationInParameter(parameter, compiled.getObjectFieldsMap().get(parameter.getId()), p);
                parameter.setRequired(castDefault(parameter.getRequired(), false));
                inFieldsMap.put(parameter.getId(), parameter);
            }
        return inFieldsMap;
    }

    /**
     * Выполнение подготовительных действий над входящим параметром операции объекта
     *
     * @param parameter Входящие параметр операции исходной модели объекта
     * @param field     Соответствующее ему поле в объекте
     * @param p         Процессор сборки метаданных
     */
    private void prepareOperationInParameter(AbstractParameter parameter, AbstractParameter field, CompileProcessor p) {
        if (parameter instanceof ObjectSimpleField && field instanceof ObjectSimpleField) {
            resolveSimpleFieldDefault((ObjectSimpleField) parameter, (ObjectSimpleField) field);
        } else if (parameter instanceof ObjectReferenceField && field instanceof ObjectReferenceField) {
            ObjectReferenceField refParam = (ObjectReferenceField) parameter;
            if (refParam.getReferenceObjectId() != null)
                initReferenceFieldByObjectId(refParam, p, 1);
            ObjectReferenceField refField = (ObjectReferenceField) field;
            resolveReferenceFieldDefault(refParam, refField);
            if (ArrayUtils.isNotEmpty(refParam.getFields())) {
                Map<String, AbstractParameter> nestedFieldsMap = Arrays.stream(
                        refField.getFields()).collect(Collectors.toMap(AbstractParameter::getId, Function.identity()));
                for (AbstractParameter refParamField : refParam.getFields())
                    if (nestedFieldsMap.containsKey(refParamField.getId()))
                        prepareOperationInParameter(refParamField, nestedFieldsMap.get(refParamField.getId()), p);
            } else if (ArrayUtils.isNotEmpty(refField.getFields())) {
                refParam.setFields(refField.getFields());
            }
        }
    }

    /**
     * Присвоение значений по умолчанию простым полям параметра, если они не были заданы
     *
     * @param parameter Параметр вызова
     * @param field     Скомпилированный объект
     */
    private void resolveSimpleFieldDefault(ObjectSimpleField parameter, ObjectSimpleField field) {
        if (field == null) return;

        resolveFieldDefault(parameter, field);
        if (parameter.getDomain() == null)
            parameter.setDomain(field.getDomain());
        if (parameter.getDefaultValue() == null)
            parameter.setDefaultValue(field.getDefaultValue());
        if (parameter.getNormalize() == null)
            parameter.setNormalize(field.getNormalize());
    }

    /**
     * Присвоение значений по умолчанию составным полям параметра, если они не были заданы
     *
     * @param parameter Параметр вызова
     * @param field     Скомпилированный объект
     */
    private void resolveReferenceFieldDefault(ObjectReferenceField parameter, ObjectReferenceField field) {
        if (field == null) return;

        resolveFieldDefault(parameter, field);
        if (parameter.getEntityClass() == null)
            parameter.setEntityClass(field.getEntityClass());
        if (parameter.getReferenceObjectId() == null)
            parameter.setReferenceObjectId(field.getReferenceObjectId());
    }

    /**
     * Присвоение значений по умолчанию полям параметра, если они не были заданы
     *
     * @param parameter Параметр вызова
     * @param field     Скомпилированный объект
     */
    private void resolveFieldDefault(AbstractParameter parameter, AbstractParameter field) {
        if (parameter.getRequired() == null)
            parameter.setRequired(field.getRequired());
        if (parameter.getMapping() == null)
            parameter.setMapping(field.getMapping());
    }
}
