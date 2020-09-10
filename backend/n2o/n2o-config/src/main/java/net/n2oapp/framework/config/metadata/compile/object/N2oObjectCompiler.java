package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperType;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectScalarField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
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
        compiled.setOperations(new StrictMap<>());
        compiled.setObjectFields(new ArrayList<>());
        compiled.setObjectFieldsMap(new HashMap<>());
        initObjectFields(source.getObjectFields(), compiled);
        compiled.setName(castDefault(source.getName(), source.getId()));
        compiled.setValidationsMap(new HashMap<>());
        compiled.setValidations(initValidations(source, compiled, context, p));
        compiled.setValidationsMap(initValidationsMap(compiled.getValidations()));
        initOperationsMap(source, compiled, context, p);
        compiled.setTableName(source.getTableName());
        compiled.setEntityClass(source.getEntityClass());
        compiled.setAppName(source.getAppName());
        compiled.setModuleName(source.getModuleName());
        compiled.setServiceClass(source.getServiceClass());
        compiled.setServiceName(source.getServiceName());
        if (context instanceof ActionContext) {
            ActionContext actionContext = (ActionContext) context;
            if (actionContext.getValidations() != null)
                compiled.setFieldValidations(actionContext.getValidations());
        }
        return compiled;
    }

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

    private void initRefField(ObjectReferenceField field, CompiledObject compiled) {
        ObjectScalarField[] referenceFields = field.getFields();
        if (referenceFields == null) {
            ObjectScalarField innerField = new ObjectScalarField();
            innerField.setId("id");
            innerField.setMapping("id");
            field.getObjectReferenceFields().add(innerField);
            field.setNullIgnore(true);
        } else
            for (ObjectScalarField objectScalarField : referenceFields)
                field.getObjectReferenceFields().add(objectScalarField);
        if (compiled.getObjectReferenceFieldsMap() == null)
            compiled.setObjectReferenceFieldsMap(new HashMap<>());
        if (field.getId().contains("."))
            field.setId(field.getId().substring(field.getId().indexOf(".") + 1, field.getId().length() - 1));
        compiled.getObjectReferenceFieldsMap().put(field.getId(), field);
    }

    private List<Validation> initValidations(N2oObject source, CompiledObject compiled, C context, CompileProcessor p) {
        List<Validation> result = new ArrayList<>();
        if (source.getN2oValidations() != null) {
            for (N2oValidation validation : source.getN2oValidations()) {
                if (validation instanceof N2oConstraint) {
                    N2oConstraint n2oConstraint = (N2oConstraint) validation;
                    if (n2oConstraint.getInParameters() != null)
                        for (N2oObject.Parameter parameter : n2oConstraint.getInParameters()) {
                            resolveDefaultParameter(parameter, compiled);
                            parameter.setRequired(p.cast(parameter.getRequired(), parameter.getDefaultValue() == null));
                        }
                }
                result.add(p.compile(validation, context));
            }
        }
        return result;
    }

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
                addOperation(compileOperation, compiled);
                initOperationValidations(operation, compileOperation, source, compiled, context, p);
            }
        }
    }

    private void initRefFieldChildParams(N2oObject.Parameter parameter, ObjectReferenceField compiledParameter, CompileProcessor p) {
        List<N2oObject.Parameter> childParams = new ArrayList<>();
        for (ObjectScalarField field : compiledParameter.getObjectReferenceFields()) {
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

    private void initOperationValidations(N2oObject.Operation operation, CompiledObject.Operation compiledOperation,
                                          N2oObject source, CompiledObject compiled, C context, CompileProcessor p) {
        List<Validation> validationList = new ArrayList<>();
        List<Validation> whiteListValidationList = new ArrayList<>();

        if (compiledOperation.getValidations() != null) {
            if (compiledOperation.getValidations().getActivate() == null
                    && compiledOperation.getValidations().getBlackList() == null
                    && compiledOperation.getValidations().getWhiteList() == null)
                compiledOperation.getValidations().setActivate(N2oObject.Operation.Validations.Activate.all);
            else if (compiledOperation.getValidations().getBlackList() != null && compiledOperation.getValidations().getWhiteList() != null)
                throw new N2oException("Whitelist is incompatible with blacklist");
            if (compiledOperation.getValidations().getActivate() != null)
                resolveActivate(operation, compiledOperation, source);

            if (compiledOperation.getValidations().getWhiteList() != null)
                fillByWhiteListValidations(compiledOperation.getValidations().getWhiteList(), compiled.getValidationsMap(), validationList,
                        compiledOperation.getValidations().getActivate(), whiteListValidationList);
            else if (compiledOperation.getValidations().getBlackList() != null)
                fillByBlackListValidations(compiledOperation.getValidations().getBlackList(), compiled.getValidationsMap(), validationList);

            List<Validation> inlineValidations = getInlineValidations(
                    compiledOperation.getValidations().getInlineValidations(), compiled, context, p);
            if (inlineValidations != null) {
                validationList.addAll(inlineValidations);
                whiteListValidationList.addAll(inlineValidations);
            }

            compiledOperation.setValidationList(validationList);
            compiledOperation.setValidationsMap(initValidationsMap(validationList));
            compiledOperation.setWhiteListValidationsMap(initValidationsMap(whiteListValidationList));
        }

        initRequiredParamValidations(compiledOperation, p);

        if (context instanceof ActionContext && ((ActionContext) context).getValidations() != null) {
            if (compiledOperation.getValidationList() == null)
                compiledOperation.setValidationList(new ArrayList<>());
            mergeValidations(compiledOperation.getValidationList(), ((ActionContext) context).getValidations());
        }
    }

    private void fillByWhiteListValidations(String[] whiteList, Map<String, Validation> validationsMap, List<Validation> validationList,
                                            N2oObject.Operation.Validations.Activate activate, List<Validation> whiteListValidationList) {
        for (String name : whiteList) {
            Validation validation = validationsMap.get(name.trim());
            if (!Boolean.FALSE.equals(validation.getEnabled())) {
                validationList.add(validation);
                if (activate != N2oObject.Operation.Validations.Activate.all)
                    whiteListValidationList.add(validation);
            }
        }
    }

    private void fillByBlackListValidations(String[] blackList, Map<String, Validation> validationsMap, List<Validation> validationList) {
        Map<String, Validation> blackListValidationsMap = new HashMap<>(validationsMap);
        for (String name : blackList)
            blackListValidationsMap.remove(name.trim());
        validationList.addAll(blackListValidationsMap.values());
    }

    private List<Validation> getInlineValidations(N2oValidation[] validations, CompiledObject compiled, C context, CompileProcessor p) {
        if (validations == null)
            return null;

        List<Validation> inlineValidations = new ArrayList<>();
        for (N2oValidation n2oValidation : validations) {
            if ("false".equals(n2oValidation.getEnabled()))
                continue;
            if (n2oValidation instanceof N2oConstraint) {
                N2oConstraint n2oConstraint = (N2oConstraint) n2oValidation;
                if (n2oConstraint.getInParameters() != null)
                    for (N2oObject.Parameter parameter : n2oConstraint.getInParameters()) {
                        resolveDefaultParameter(parameter, compiled);
                        parameter.setRequired(p.cast(parameter.getRequired(), parameter.getDefaultValue() == null));
                    }
            }
            inlineValidations.add(p.compile(n2oValidation, context));
        }
        return inlineValidations;
    }

    private void initRequiredParamValidations(CompiledObject.Operation compiledOperation, CompileProcessor p) {
        List<Validation> requiredParamValidations = new ArrayList<>();
        for (N2oObject.Parameter parameter : compiledOperation.getInParametersMap().values()) {
            if (parameter.getRequired() != null && parameter.getRequired()) {
                MandatoryValidation validation = new MandatoryValidation(parameter.getId(),
                        p.getMessage("n2o.required.field"), parameter.getId());
                validation.setMoment(N2oValidation.ServerMoment.beforeOperation);
                requiredParamValidations.add(validation);
            }
        }

        if (!requiredParamValidations.isEmpty()) {
            if (compiledOperation.getValidationList() == null)
                compiledOperation.setValidationList(new ArrayList<>());
            compiledOperation.getValidationList().addAll(requiredParamValidations);
        }
    }

    private void mergeValidations(List<Validation> operationValidations, List<Validation> controlValidations) {
        for (Validation cv : controlValidations) {
            operationValidations.removeIf(validation -> validation.getId().equals(cv.getId()));
            operationValidations.add(cv);
        }
    }

    private void resolveActivate(N2oObject.Operation operation, CompiledObject.Operation compiledOperation, N2oObject source) {
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

    private static Map<String, Validation> initValidationsMap(List<Validation> validations) {
        Map<String, Validation> res = new HashMap<>();
        for (Validation validation : validations) {
            res.put(validation.getId(), validation);
        }
        return res;
    }

    private CompiledObject.Operation compileOperation(N2oObject.Operation operation, CompiledObject compiled, CompileProcessor processor) {
        Map<String, N2oObject.Parameter> inParamMap = new LinkedHashMap<>();
        if ((operation.getInParameters() != null) && (operation.getInParameters().length > 0))
            for (N2oObject.Parameter parameter : operation.getInParameters())
                addParameter(parameter, compiled, p -> {
                    p.setRequired(castDefault(p.getRequired(), false));
                    inParamMap.put(p.getId(), p);
                });

        Map<String, N2oObject.Parameter> outParamMap = new LinkedHashMap<>();
        if (operation.getOutParameters() != null)
            for (N2oObject.Parameter parameter : operation.getOutParameters())
                addParameter(parameter, compiled, p -> outParamMap.put(p.getId(), p));

        CompiledObject.Operation compiledOperation = new CompiledObject.Operation(inParamMap, outParamMap);
        initDefaultOperationProperties(compiledOperation, operation, processor);
        if (compiledOperation.getInParametersMap() != null)
            for (N2oObject.Parameter parameter : compiledOperation.getInParametersMap().values())
                resolveDefaultParameter(parameter, compiled);

        compiledOperation.setProperties((processor.mapAttributes(operation)));
        return compiledOperation;
    }

    private void initDefaultOperationProperties(CompiledObject.Operation compiledOperation, N2oObject.Operation operation, CompileProcessor p) {
        compiledOperation.setId(operation.getId());
        compiledOperation.setDescription(operation.getDescription());
        compiledOperation.setName(operation.getName());
        compiledOperation.setConfirm(operation.getConfirm());
        compiledOperation.setConfirmationText(castDefault(operation.getConfirmationText(),
                p.getMessage("n2o.confirm.text")));
        compiledOperation.setBulkConfirmationText(castDefault(operation.getBulkConfirmationText(),
                p.getMessage("n2o.confirmationGroup")));
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

    private <T extends InvocationParameter> void addParameter(T parameter, CompiledObject compiled, Consumer<T> consumer) {
        resolveDefaultParameter(parameter, compiled);
        consumer.accept(parameter);
    }

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
        if (parameter.getMapper() == null)
            parameter.setMapper(resolveDefaultMapper(field));
    }

    private String resolveDefaultDomain(AbstractParameter field) {
        String domain = null;
        if (field instanceof ObjectScalarField && ((ObjectScalarField) field).getDomain() != null)
            domain = ((ObjectScalarField) field).getDomain();
        return domain;
    }

    private Boolean resolveDefaultRequired(AbstractParameter field) {
        Boolean required = null;
        if (field != null)
            required = field.getRequired();
        return required;
    }

    private String resolveDefaultValue(AbstractParameter field) {
        String defaultValue = null;
        if (field instanceof ObjectScalarField && ((ObjectScalarField) field).getDefaultValue() != null)
            defaultValue = ((ObjectScalarField) field).getDefaultValue();
        return defaultValue;
    }

    private String resolveDefaultNormalize(AbstractParameter field) {
        String normalizer = null;
        if (field instanceof ObjectScalarField && ((ObjectScalarField) field).getNormalize() != null)
            normalizer = ((ObjectScalarField) field).getNormalize();
        return normalizer;
    }

    private MapperType resolveDefaultMapper(AbstractParameter field) {
        MapperType mapper = null;
        if (field instanceof ObjectScalarField && ((ObjectScalarField) field).getMapperType() != null)
            mapper = ((ObjectScalarField) field).getMapperType();
        return mapper;
    }

    private String resolveDefaultMapping(AbstractParameter field) {
        String mapping = null;
        if (field != null)
            mapping = field.getMapping();
        return mapping;
    }

    private void addOperation(CompiledObject.Operation compiledOperation, CompiledObject compiled) {
        compiled.getOperations().put(compiledOperation.getId(), compiledOperation);
    }
}
