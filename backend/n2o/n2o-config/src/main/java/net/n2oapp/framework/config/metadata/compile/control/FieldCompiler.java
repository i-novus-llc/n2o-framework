package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetVisibilityScope;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.util.ControlFilterUtil;

import java.util.*;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class FieldCompiler<D extends Field, S extends N2oField> extends ComponentCompiler<D, S> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.src";
    }

    protected void compileField(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        compileComponent(field, source, context, p);

        field.setVisible(source.getVisible());
        field.setEnabled(source.getEnabled());

        field.setLabel(initLabel(source, p));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        compileDefaultValues(field, source, p);
        compileDependencies(field, source, p);
        initValidations(source, field, context, p);
        compileFilters(field, p);
    }

    /**
     * Сборка значения по умолчанию у поля
     *
     * @param source Исходная модель поля
     * @param p      Процессор сборки
     * @return Значение по умолчанию поля
     */
    protected Object compileDefValues(S source, CompileProcessor p) {
        return null;
    }

    private void compileDefaultValues(Field field, S source, CompileProcessor p) {
        UploadScope uploadScope = p.getScope(UploadScope.class);
        if (uploadScope != null && !UploadType.defaults.equals(uploadScope.getUpload()))
            return;
        ModelsScope defaultValues = p.getScope(ModelsScope.class);
        if (defaultValues != null && defaultValues.hasModels()) {
            Object defValue;
            if (source.getDefaultValue() != null) {
                defValue = p.resolve(source.getDefaultValue(), source.getDomain());
            } else {
                defValue = compileDefValues(source, p);
            }
            if (defValue != null) {
                if (defValue instanceof String) {
                    defValue = ScriptProcessor.resolveExpression((String) defValue);
                }
                if (StringUtils.isJs(defValue)) {
                    ModelLink defaultValue = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId());
                    defaultValue.setValue(defValue);
                    defaultValues.add(field.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(field.getId(), p);
                    ModelLink modelLink = new ModelLink(defaultValues.getModel(), defaultValues.getWidgetId(), field.getId());
                    if (defValue instanceof DefaultValues) {
                        DefaultValues defaultValue = (DefaultValues) defValue;
                        Map<String, Object> values = defaultValue.getValues();
                        if (defaultValue.getValues() != null) {
                            for (String param : values.keySet()) {
                                if (values.get(param) instanceof String) {
                                    Object value = ScriptProcessor.resolveExpression((String) values.get(param));
                                    if (value != null)
                                        values.put(param, value);
                                }
                            }
                        }
                    }
                    modelLink.setValue(defValue);
                    modelLink.setSubModelQuery(subModelQuery);
                    defaultValues.add(field.getId(), modelLink);
                }
            }
        }
    }

    private String initLabel(S source, CompileProcessor p) {
        if (source.getNoLabel() == null || !source.getNoLabel()) {
            String label = p.resolveJS(source.getLabel());
            FieldSetScope scope = p.getScope(FieldSetScope.class);
            if (label == null && scope != null) {
                label = scope.get(source.getId());
            }
            if (label == null)
                label = source.getId();
            return label;
        } else
            return null;
    }

    private void compileFilters(Field field, CompileProcessor p) {
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        if (filtersScope != null) {
            CompiledQuery query = p.getScope(CompiledQuery.class);
            if (query == null)
                return;
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            List<N2oQuery.Filter> filters = ControlFilterUtil.getFilters(field.getId(), query);
            filters.forEach(f -> {
                Filter filter = new Filter();
                filter.setFilterId(f.getFilterField());
                filter.setParam(widgetScope.getClientWidgetId() + "_" + f.getParam());
                filter.setReloadable(true);
                SubModelQuery subModelQuery = findSubModelQuery(field.getId(), p);
                ModelLink link = new ModelLink(ReduxModel.FILTER, widgetScope.getClientWidgetId());
                link.setSubModelQuery(subModelQuery);
                link.setValue(p.resolveJS(Placeholders.ref(f.getFilterField())));
                filter.setLink(link);
                filtersScope.getFilters().add(filter);
            });
        }
    }

    /**
     * Возвращает информацию о вложенных моделях выборки по идентификатору поля
     *
     * @param fieldId - идентификатор поля
     * @param p       - процессор сборки метаданных
     */
    protected SubModelQuery findSubModelQuery(String fieldId, CompileProcessor p) {
        if (fieldId == null) return null;
        SubModelsScope subModelsScope = p.getScope(SubModelsScope.class);
        if (subModelsScope != null) {
            return subModelsScope.stream()
                    .filter(subModelQuery -> fieldId.equals(subModelQuery.getSubModel()))
                    .findAny()
                    .orElse(null);
        }
        return null;
    }

    private void initValidations(S source, Field field, CompileContext<?, ?> context, CompileProcessor p) {
        List<Validation> serverValidations = new ArrayList<>();
        List<Validation> clientValidations = new ArrayList<>();
        Set<String> visibilityConditions = p.getScope(FieldSetVisibilityScope.class);
        MomentScope momentScope = p.getScope(MomentScope.class);
        String REQUIRED_MESSAGE = momentScope != null
                && N2oValidation.ServerMoment.beforeQuery.equals(momentScope.getMoment())
                ? "n2o.required.filter" : "n2o.required.field";
        if (source.getRequired() != null && source.getRequired()) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(REQUIRED_MESSAGE), field.getId());
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            collectEnablingConditions(mandatory, source,
                    N2oField.VisibilityDependency.class,
                    N2oField.EnablingDependency.class);
            mandatory.addEnablingConditions(visibilityConditions);
            serverValidations.add(mandatory);
            clientValidations.add(mandatory);
            field.setRequired(true);
        } else if (source.containsDependency(N2oField.RequiringDependency.class)) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(REQUIRED_MESSAGE), field.getId());
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            mandatory.addEnablingConditions(visibilityConditions);
            collectEnablingConditions(mandatory, source,
                    N2oField.RequiringDependency.class,
                    N2oField.VisibilityDependency.class,
                    N2oField.EnablingDependency.class);
            if (mandatory.getEnablingConditions() != null && !mandatory.getEnablingConditions().isEmpty()) {
                serverValidations.add(mandatory);
                clientValidations.add(mandatory);
            }
        }
        CompiledObject object = p.getScope(CompiledObject.class);
        initInlineValidations(field, source, serverValidations, clientValidations, object, context, visibilityConditions, p);
        field.setServerValidations(serverValidations.isEmpty() ? null : serverValidations);
        field.setClientValidations(clientValidations.isEmpty() ? null : clientValidations);
    }

    private void collectEnablingConditions(Validation v, S source, Class ... types) {
        if (source.getDependencies() != null && types != null) {
            for (N2oField.Dependency dependency : source.getDependencies()) {
                for (Class clazz : types) {
                    if (dependency.getClass().equals(clazz)) {
                        v.addEnablingCondition(dependency.getValue());
                    }
                }
            }
        }
    }

    private void initWhiteListValidation(String fieldId,
                                         String refId,
                                         S source,
                                         List<Validation> serverValidations,
                                         List<Validation> clientValidations,
                                         CompiledObject object,
                                         Set<String> visibilityConditions) {
        if (object == null) {
            throw new N2oException("Field {0} have validation reference, but haven't object!").addData(fieldId);
        }
        Validation objectValidation = null;
        if (object.getValidationsMap() != null && object.getValidationsMap().containsKey(refId)) {
            objectValidation = object.getValidationsMap().get(refId);
        } else {
            if (object.getOperations() != null && !object.getOperations().isEmpty()) {
                for (CompiledObject.Operation operation : object.getOperations().values()) {
                    Optional<Validation> result = operation.getValidationList().stream().filter(v -> v.getId().equals(refId)).findFirst();
                    if (result.isPresent()) {
                        objectValidation = result.get();
                        break;
                    }
                }
            }
        }
        if (objectValidation == null) {
            throw new N2oException("Field {0} contains validation reference for nonexistent validation!").addData(fieldId);
        }
        Validation validation = null;
        if (objectValidation instanceof ConstraintValidation) {
            validation = new ConstraintValidation((ConstraintValidation) objectValidation);
        } else if (objectValidation instanceof ConditionValidation) {
            validation = new ConditionValidation((ConditionValidation) objectValidation);
        } else if (objectValidation instanceof MandatoryValidation) {
            validation = new MandatoryValidation((MandatoryValidation) objectValidation);
        }
        if (validation == null)
            return;
        List<String> enablingConditions = new ArrayList<>();
        if (source.getDependencies() != null) {
            for (N2oField.Dependency dependency : source.getDependencies()) {
                if (dependency instanceof N2oField.VisibilityDependency) {
                    enablingConditions.add(dependency.getValue());
                }
            }
        }
        validation.setFieldId(fieldId);
        validation.addEnablingConditions(enablingConditions);
        validation.addEnablingConditions(visibilityConditions);
        if (validation.getSide() == null || validation.getSide().equals("client,server")) {
            serverValidations.add(validation);
            clientValidations.add(validation);
        } else if (validation.getSide().equals("client")) {
            clientValidations.add(validation);
        } else if (validation.getSide().equals("server")) {
            serverValidations.add(validation);
        }
    }

    private void initInlineValidations(Field field,
                                       S source,
                                       List<Validation> serverValidations,
                                       List<Validation> clientValidations,
                                       CompiledObject object,
                                       CompileContext<?, ?> context,
                                       Set<String> visibilityConditions,
                                       CompileProcessor p) {

        N2oField.Validations validations = source.getValidations();
        if (validations == null) return;
        if (validations.getWhiteList() != null) {
            for (String validation : validations.getWhiteList()) {
                initWhiteListValidation(field.getId(),
                        validation,
                        source,
                        serverValidations, clientValidations, object, visibilityConditions);
            }
        }
        if (validations.getInlineValidations() != null) {
            List<String> enablingConditions = new ArrayList<>();
            if (source.getDependencies() != null) {
                for (N2oField.Dependency dependency : source.getDependencies()) {
                    if (dependency.getClass().equals(N2oField.VisibilityDependency.class))
                        enablingConditions.add(dependency.getValue());
                }
            }
            if (object.getValidations() == null)
                object.setValidations(new ArrayList<>());
            for (N2oValidation v : validations.getInlineValidations()) {
                v.setFieldId(field.getId());
                Validation compiledValidation = p.compile(v, context);
                if (field.getVisible() != null && !field.getVisible()) {
                    continue;
                } else if (!enablingConditions.isEmpty()) {
                    compiledValidation.addEnablingConditions(enablingConditions);
                }
                compiledValidation.addEnablingConditions(visibilityConditions);
                object.addValidation(compiledValidation);
                serverValidations.add(compiledValidation);
                if (compiledValidation.getSide() == null || compiledValidation.getSide().contains("client"))
                    clientValidations.add(compiledValidation);
            }
        }
    }

    /**
     * Компиляция зависимостей между полями
     *
     * @param field  клиентская модель элемента ввода
     * @param source исходная модель поля
     */
    protected void compileDependencies(Field field, S source, CompileProcessor p) {

        if (source.getDependencies() != null) {
            for (N2oField.Dependency d : source.getDependencies()) {
                ControlDependency dependency = new ControlDependency();
                if (d instanceof N2oField.EnablingDependency)
                    dependency.setType(ValidationType.enabled);
                else if (d instanceof N2oField.RequiringDependency)
                    dependency.setType(ValidationType.required);
                else if (d instanceof N2oField.VisibilityDependency)
                    dependency.setType(ValidationType.visible);
                else if (d instanceof N2oField.SetValueDependency)
                    dependency.setType(ValidationType.setValue);

                dependency.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
                dependency.setApplyOnInit(p.cast(d.getApplyOnInit(), true));
                if (d.getOn() != null) {
                    List<String> ons = Arrays.asList(d.getOn());
                    ons.replaceAll(String::trim);
                    dependency.getOn().addAll(ons);
                }

                field.addDependency(dependency);
            }
        }

        if (source.getDependsOn() != null) {
            ControlDependency dependency = new ControlDependency();
            List<String> ons = Arrays.asList(source.getDependsOn());
            ons.replaceAll(String::trim);
            dependency.setOn(ons);
            dependency.setType(ValidationType.reRender);
            field.addDependency(dependency);
        }
    }
}
