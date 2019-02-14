package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
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
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActions;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Сборка объекта
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
        compiled.setOperations(new HashMap<>());
        compiled.setObjectFields(new ArrayList<>());
        compiled.setObjectFieldsMap(new HashMap<>());
        if (source.getObjectFields() != null) {
            for (AbstractParameter field : source.getObjectFields()) {
                compiled.getObjectFields().add(field);
                compiled.getObjectFieldsMap().put(field.getId(), field);
                if (field instanceof ObjectReferenceField) {
                    ObjectScalarField[] referenceFields = ((ObjectReferenceField) field).getFields();
                    if (referenceFields == null) {
                        ObjectScalarField innerField = new ObjectScalarField();
                        innerField.setId("id");
                        innerField.setMapping("id");
                        ((ObjectReferenceField) field).getObjectReferenceFields().add(innerField);
                        field.setNullIgnore(true);
                    } else {
                        for (ObjectScalarField objectScalarField : referenceFields) {
                            ((ObjectReferenceField) field).getObjectReferenceFields().add(objectScalarField);
                        }
                    }
                    if (compiled.getObjectReferenceFieldsMap() == null)
                        compiled.setObjectReferenceFieldsMap(new HashMap<>());
                    if (field.getId().contains(".")) {
                        field.setId(field.getId().substring(field.getId().indexOf(".") + 1, field.getId().length() - 1));
                    }
                    compiled.getObjectReferenceFieldsMap().put(field.getId(), (ObjectReferenceField) field);
                }
            }
        }
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
            if (actionContext.getValidations() != null) {
                compiled.setFieldValidations(actionContext.getValidations());
            }
        }
        return compiled;
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
                if (operation.getInParameters() != null) {
                    for (N2oObject.Parameter parameter : operation.getInParameters()) {
                        AbstractParameter compiledParameter = compiled.getObjectFieldsMap().get(parameter.getId());
                        if (compiledParameter != null) {
                            parameter.setPluralityType(compiledParameter.getPluralityType());
                            parameter.setNullIgnore(p.cast(parameter.getNullIgnore(), compiledParameter.getNullIgnore()));
                            if (compiledParameter instanceof ObjectReferenceField) {
                                List<N2oObject.Parameter> childParams = new ArrayList<>();
                                for (ObjectScalarField field : ((ObjectReferenceField) compiledParameter).getObjectReferenceFields()) {
                                    N2oObject.Parameter childParam = new N2oObject.Parameter();
                                    childParam.setId(field.getId());
                                    childParam.setMapping(field.getMapping());
                                    childParam.setNormalize(field.getNormalize());
                                    childParam.setDomain(field.getDomain());
                                    childParam.setDefaultValue(field.getDefaultValue());
                                    childParams.add(childParam);
                                }
                                parameter.setEntityClass(
                                        p.cast(parameter.getEntityClass(),
                                                ((ObjectReferenceField) compiledParameter).getEntityClass())
                                );
                                parameter.setMapping(
                                        p.cast(parameter.getMapping(),
                                                compiledParameter.getMapping())
                                );
                                parameter.setChildParams(childParams.toArray(new N2oObject.Parameter[0]));
                            }
                        }
                    }
                }
                addOperation(compileOperation, compiled);
                initOperationValidations(operation, compileOperation, source, compiled, context, p);
            }
        }
    }

    private void initOperationValidations(N2oObject.Operation operation, CompiledObject.Operation compiledOperation, N2oObject source, CompiledObject compiled, C context, CompileProcessor p) {
        List<Validation> validationList = new ArrayList<>();
        List<Validation> whiteListValidationList = new ArrayList<>();
        List<Validation> inlineValidations = null;
        if (compiledOperation.getValidations() != null) {
            if (compiledOperation.getValidations().getActivate() == null
                    && compiledOperation.getValidations().getBlackList() == null
                    && compiledOperation.getValidations().getWhiteList() == null)
                compiledOperation.getValidations().setActivate(N2oObject.Operation.Validations.Activate.all);
            else if (compiledOperation.getValidations().getBlackList() != null && compiledOperation.getValidations().getWhiteList() != null)
                throw new N2oException("Whitelist is incompatible with blacklist");
            if (compiledOperation.getValidations().getActivate() != null)
                resolveActivate(operation, compiledOperation, source);
            if (compiledOperation.getValidations().getWhiteList() != null) {
                for (String name : operation.getValidations().getWhiteList()) {
                    Validation validation = compiled.getValidationsMap().get(name.trim());
                    if (validation.getEnabled() == null || validation.getEnabled()) {
                        validationList.add(validation);
                        if (compiledOperation.getValidations().getActivate() != N2oObject.Operation.Validations.Activate.all)
                            whiteListValidationList.add(validation);
                    }
                }
            }
            if (compiledOperation.getValidations().getBlackList() != null) {
                Map<String, Validation> blackListValidationsMap = new HashMap<>();
                blackListValidationsMap.putAll(compiled.getValidationsMap());
                for (String name : operation.getValidations().getBlackList()) {
                    blackListValidationsMap.remove(name.trim());
                }
                validationList.addAll(blackListValidationsMap.values());
            }
            if (compiledOperation.getValidations().getInlineValidations() != null) {
                inlineValidations = new ArrayList<>();
                for (N2oValidation n2oValidation : operation.getValidations().getInlineValidations()) {
                    if (n2oValidation.getEnabled() != null && n2oValidation.getEnabled().equals("false"))
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

            }
            if (inlineValidations != null) {
                validationList.addAll(inlineValidations);
                whiteListValidationList.addAll(inlineValidations);
            }
            if (validationList.size() == 0)
                compiledOperation.setAuto(true);
            compiledOperation.setValidationList(validationList);
            compiledOperation.setValidationsMap(initValidationsMap(validationList));
            compiledOperation.setWhiteListValidationsMap(initValidationsMap(whiteListValidationList));
            List<ConditionValidation> conditionList = new ArrayList<>();
            for (Validation validation : validationList) {
                if (validation instanceof ConditionValidation) {
                    conditionList.add((ConditionValidation) validation);
                }
            }
            compiledOperation.setConditionList(conditionList);
        }

        List<Validation> requiredParamValidations = new ArrayList<>();
        for (N2oObject.Parameter parameter : compiledOperation.getInParametersMap().values()) {
            if (parameter.getRequired() != null && parameter.getRequired()) {
                MandatoryValidation validation = new MandatoryValidation(parameter.getId(), p.getMessage("n2o.required.field"), parameter.getId());
                validation.setMoment(N2oValidation.ServerMoment.beforeOperation);
                requiredParamValidations.add(validation);
            }
        }

        if (requiredParamValidations.size() > 0) {
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

    private void mergeValidations(List<Validation> operationValidations, List<Validation> controlValidations) {
        for (Validation cv : controlValidations) {
            operationValidations.removeIf(validation -> validation.getId().equals(cv.getId()));
            operationValidations.add(cv);
        }
    }


    private void resolveActivate(N2oObject.Operation operation, CompiledObject.Operation compiledOperation, N2oObject source) {
        Integer length;
        int i = 0;
        switch (compiledOperation.getValidations().getActivate()) {
            case all:
                if (compiledOperation.getValidations().getWhiteList() == null && compiledOperation.getValidations().getBlackList() == null && source.getN2oValidations() != null) {
                    length = source.getN2oValidations().length;
                    String[] whiteList = new String[length];
                    i = 0;
                    for (N2oValidation n2oValidation : source.getN2oValidations()) {
                        whiteList[i] = n2oValidation.getId();
                        i++;
                    }
                    compiledOperation.getValidations().setWhiteList(whiteList);
                }
                break;
            case whiteList:
                length = operation.getValidations().getRefValidations().length;
                String[] whiteList = new String[length];
                for (N2oObject.Operation.Validations.Validation validation : operation.getValidations().getRefValidations()) {
                    whiteList[i] = validation.getRefId();
                    i++;
                }
                compiledOperation.getValidations().setWhiteList(whiteList);
                break;
            case blackList:
                length = operation.getValidations().getRefValidations().length;
                String[] blackList = new String[length];
                for (N2oObject.Operation.Validations.Validation validation : operation.getValidations().getRefValidations()) {
                    blackList[i] = validation.getRefId();
                    i++;
                }
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
        if ((operation.getInParameters() != null) && (operation.getInParameters().length > 0)) {
            for (N2oObject.Parameter parameter : operation.getInParameters()) {
                addParameter(parameter, compiled, p -> {
                    p.setRequired(castDefault(p.getRequired(), false));
                    inParamMap.put(p.getId(), p);
                });
            }
        }
        Map<String, N2oObject.Parameter> outParamMap = new LinkedHashMap<>();
        if (operation.getOutParameters() != null) {
            for (N2oObject.Parameter parameter : operation.getOutParameters()) {
                addParameter(parameter, compiled, p -> outParamMap.put(p.getId(), p));
            }
        }
        CompiledObject.Operation compiledOperation = new CompiledObject.Operation(inParamMap, outParamMap);
        //compiledOperation = compiler.cloneToInheritedClass(action, compiledOperation); //todo
        initDefaultOperationProperties(compiledOperation, operation, processor);
        compiledOperation.setObjectId(compiled.getId());
        if (compiledOperation.getInParametersMap() != null) {
            for (N2oObject.Parameter parameter : compiledOperation.getInParametersMap().values()) {
                resolveDefaultParameter(parameter, compiled);
            }
        }
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
        for (DefaultActions defaultOperations : DefaultActions.values()) {
            if (!defaultOperations.name().equals(operation.getId())) continue;
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
