package net.n2oapp.framework.config.metadata.compile.control;

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
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetVisibilityScope;
import net.n2oapp.framework.config.metadata.compile.widget.FiltersScope;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.ControlFilterUtil;

import java.util.*;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class FieldCompiler<D extends Field, S extends N2oField> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    private final String FIELD_REQUIRED_MESSAGE = "n2o.required";

    protected void compileField(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        field.setId(source.getId());
        field.setSrc(source.getSrc());
        field.setVisible(source.getVisible());
        field.setEnabled(source.getEnabled());
        field.setProperties(p.mapAttributes(source));
        compileDependencies(field, source);
        initValidations(source, field, context, p);
        compileFilters(field, p);
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
        if (source.getRequired() != null && source.getRequired()) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(FIELD_REQUIRED_MESSAGE), field.getId());
            mandatory.setMoment(N2oValidation.ServerMoment.beforeOperation);
            collectEnablingConditions(mandatory, source, N2oField.VisibilityDependency.class);
            mandatory.addEnablingConditions(visibilityConditions);
            serverValidations.add(mandatory);
            clientValidations.add(mandatory);
            field.setRequired(true);
        } else if (source.getDependencies() != null) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(FIELD_REQUIRED_MESSAGE), field.getId());
            mandatory.setMoment(N2oValidation.ServerMoment.beforeOperation);
            mandatory.addEnablingConditions(visibilityConditions);
            collectEnablingConditions(mandatory, source, N2oField.RequiringDependency.class);
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

    private void collectEnablingConditions(Validation v, S source, Class clazz) {
        if (source.getDependencies() != null) {
            for (N2oField.Dependency dependency : source.getDependencies()) {
                if (dependency.getClass().equals(clazz)) {
                    v.addEnablingCondition(dependency.getValue());
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
    protected void compileDependencies(Field field, S source) {

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
                dependency.getOn().add(d.getOn());
                dependency.setApplyOnInit(true);
                field.addDependency(dependency);
            }
        }

        if (source.getDependsOn() != null) {
            ControlDependency dependency = new ControlDependency();
            dependency.setOn(Arrays.asList(source.getDependsOn()));
            dependency.setType(ValidationType.reRender);
            field.addDependency(dependency);
        }
    }
}
