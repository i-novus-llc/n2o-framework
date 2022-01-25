package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetVisibilityScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.util.CompileUtil;
import net.n2oapp.framework.config.util.ControlFilterUtil;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class FieldCompiler<D extends Field, S extends N2oField> extends ComponentCompiler<D, S, CompileContext<?, ?>> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.src";
    }

    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setRefPage(p.cast(source.getRefPage(), N2oField.Page.THIS));
        source.setRefDatasource(p.cast(source.getRefDatasource(), () -> {
            if (source.getRefPage().equals(N2oField.Page.THIS)) {
                return initLocalDatasourceId(p);
            } else if (source.getRefPage().equals(N2oField.Page.PARENT)) {
                if (context instanceof PageContext) {
                    return ((PageContext) context).getParentLocalDatasourceId();
                } else {
                    throw new N2oException(String.format("Field %s has ref-page=\"parent\" but PageContext not found", source.getId()));
                }
            }
            return null;
        }));
        source.setRefModel(p.cast(source.getRefModel(),
                Optional.of(p.getScope(WidgetScope.class)).map(WidgetScope::getModel).orElse(null),
                ReduxModel.resolve));
        initCondition(source, source::getVisible, new N2oField.VisibilityDependency(), b -> source.setVisible(b.toString()), !"false".equals(source.getVisible()));
        initCondition(source, source::getEnabled, new N2oField.EnablingDependency(), b -> source.setEnabled(b.toString()), !"false".equals(source.getEnabled()));
        initCondition(source, source::getRequired, new N2oField.RequiringDependency(), b -> source.setRequired(b.toString()), "true".equals(source.getRequired()));
    }

    protected void compileField(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        compileComponent(field, source, context, p);

        field.setId(source.getId());
        field.setLabel(initLabel(source, p));
        field.setNoLabelBlock(p.cast(source.getNoLabelBlock(),
                p.resolve(property("n2o.api.field.no_label_block"), Boolean.class)));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        field.setVisible(p.resolve(source.getVisible(), Boolean.class));
        field.setEnabled(p.resolve(source.getEnabled(), Boolean.class));
        field.setRequired(p.resolve(source.getRequired(), Boolean.class));
        compileFieldToolbar(field, source, context, p);
        compileDependencies(field, source, context, p);
    }

    private void initCondition(S source, Supplier<String> conditionGetter, N2oField.Dependency dependency,
                               Consumer<Boolean> conditionSetter, Boolean defaultValue) {
        if (StringUtils.isLink(conditionGetter.get())) {
            Set<String> onFields = ScriptProcessor.extractVars(conditionGetter.get());
            dependency.setValue(StringUtils.unwrapLink(conditionGetter.get()));
            dependency.setOn(onFields.toArray(String[]::new));
            source.addDependency(dependency);
            conditionSetter.accept(false);
        } else if (conditionGetter.get() != null) {
            dependency.setValue(conditionGetter.get());
            source.addDependency(dependency);
            conditionSetter.accept(defaultValue);
        } else {
            conditionSetter.accept(defaultValue);
        }
    }

    protected String initLabel(S source, CompileProcessor p) {
        if (!Boolean.TRUE.equals(source.getNoLabelBlock()) || !Boolean.TRUE.equals(source.getNoLabel()))
            return p.resolveJS(source.getLabel());
        return null;
    }

    /**
     * Компиляция зависимостей между полями
     *
     * @param field  клиентская модель элемента ввода
     * @param source исходная модель поля
     */
    protected void compileDependencies(Field field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getDependencies() != null) {
            for (N2oField.Dependency d : source.getDependencies()) {
                ControlDependency dependency = d instanceof N2oField.FetchValueDependency ?
                        compileFetchDependency(d, context, p) :
                        compileControlDependency(field, d, p);
                addToField(dependency, field, d, p);
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

    private void addToField(ControlDependency compiled, Field field, N2oField.Dependency source, CompileProcessor p) {
        compiled.setApplyOnInit(p.cast(source.getApplyOnInit(), true));
        if (source.getOn() != null) {
            List<String> ons = Arrays.asList(source.getOn());
            ons.replaceAll(String::trim);
            compiled.getOn().addAll(ons);
        }
        field.addDependency(compiled);
    }

    private void compileFieldToolbar(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getToolbar() != null) {
            Toolbar toolbar = p.compile(source.getToolbar(), context);
            field.setToolbar(toolbar.getGroups().toArray(Group[]::new));
        }
    }

    private ControlDependency compileControlDependency(Field field, N2oField.Dependency source, CompileProcessor p) {
        ControlDependency dependency = new ControlDependency();
        if (source instanceof N2oField.EnablingDependency)
            dependency.setType(ValidationType.enabled);
        else if (source instanceof N2oField.RequiringDependency)
            dependency.setType(ValidationType.required);
        else if (source instanceof N2oField.VisibilityDependency) {
            dependency.setType(ValidationType.visible);
            Boolean isResettable = p.cast(((N2oField.VisibilityDependency) source).getReset(),
                    p.resolve(property("n2o.api.control.visibility.auto_reset"), Boolean.class));
            if (Boolean.TRUE.equals(isResettable)) {
                ControlDependency reset = new ControlDependency();
                reset.setType(ValidationType.reset);
                reset.setExpression(ScriptProcessor.resolveFunction(source.getValue()));
                addToField(reset, field, source, p);
            }
        } else if (source instanceof N2oField.SetValueDependency)
            dependency.setType(ValidationType.setValue);
        else if (source instanceof N2oField.FetchDependency)
            dependency.setType(ValidationType.fetch);
        else if (source instanceof N2oField.ResetDependency) {
            dependency.setType(ValidationType.reset);
            if (source.getValue() == null) {
                source.setValue(String.valueOf(Boolean.TRUE));
            }
        }
        dependency.setExpression(ScriptProcessor.resolveFunction(source.getValue()));
        return dependency;
    }

    private FetchValueDependency compileFetchDependency(N2oField.Dependency d, CompileContext<?, ?> context, CompileProcessor p) {
        FetchValueDependency dependency = new FetchValueDependency();
        dependency.setType(ValidationType.fetchValue);
        dependency.setValueFieldId(p.cast(((N2oField.FetchValueDependency) d).getValueFieldId(), "name"));
        dependency.setDataProvider(compileFetchDependencyDataProvider((N2oField.FetchValueDependency) d, context, p));
        return dependency;
    }

    private ClientDataProvider compileFetchDependencyDataProvider(N2oField.FetchValueDependency field,
                                                                  CompileContext<?, ?> context, CompileProcessor p) {
        N2oClientDataProvider dataProvider = N2oClientDataProviderUtil.initFromField(field.getPreFilters(), field.getQueryId(), p);
        dataProvider.setSize(field.getSize());
        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }

    protected void compileFilters(S source, CompileProcessor p) {
        TableFiltersScope filtersScope = p.getScope(TableFiltersScope.class);
        if (filtersScope != null) {
            CompiledQuery query = p.getScope(CompiledQuery.class);
            if (query == null)
                return;
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            List<N2oQuery.Filter> filters = ControlFilterUtil.getFilters(source.getId(), query);
            filters.forEach(f -> {
                Filter filter = new Filter();
                filter.setFilterId(f.getFilterField());
                filter.setParam(p.cast(source.getParam(), widgetScope.getWidgetId() + "_" + f.getParam()));
                filter.setRoutable(true);
                SubModelQuery subModelQuery = findSubModelQuery(source.getId(), p);
                ModelLink link = new ModelLink(ReduxModel.filter, widgetScope.getClientWidgetId());
                link.setSubModelQuery(subModelQuery);
                link.setValue(p.resolveJS(Placeholders.ref(f.getFilterField())));
                link.setParam(filter.getParam());
                link.setObserve(true);
                filter.setLink(link);
                filtersScope.addFilter(filter);
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
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (subModelsScope != null && widgetScope != null && subModelsScope.get(widgetScope.getDatasourceId()) != null) {
            return subModelsScope.get(widgetScope.getDatasourceId()).stream()
                    .filter(subModelQuery -> fieldId.equals(subModelQuery.getSubModel()))
                    .findAny()
                    .orElse(null);
        }
        return null;
    }

    protected void initValidations(S source, Field field, CompileContext<?, ?> context, CompileProcessor p) {
        List<Validation> validations = new ArrayList<>();
        Set<String> visibilityConditions = p.getScope(FieldSetVisibilityScope.class) != null ? p.getScope(FieldSetVisibilityScope.class).getConditions() : Collections.emptySet();
        validations.addAll(initRequiredValidation(field, source, p, visibilityConditions));
        validations.addAll(initInlineValidations(field, source, context, p, visibilityConditions));
        ValidationScope validationScope = p.getScope(ValidationScope.class);
        if (validationScope != null)
            validationScope.addAll(validations);
    }

    private List<Validation> initRequiredValidation(Field field, S source, CompileProcessor p, Set<String> visibilityConditions) {
        List<Validation> result = new ArrayList<>();
        MomentScope momentScope = p.getScope(MomentScope.class);
        String requiredMessage = momentScope != null
                && N2oValidation.ServerMoment.beforeQuery.equals(momentScope.getMoment())
                ? "n2o.required.filter" : "n2o.required.field";
        if ("true".equals(source.getRequired())) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(requiredMessage), field.getId());
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            mandatory.addEnablingConditions(visibilityConditions);
            mandatory.addEnablingConditions(collectConditions(source, N2oField.VisibilityDependency.class));
            result.add(mandatory);
            field.setRequired(true);
        } else if (source.containsDependency(N2oField.RequiringDependency.class)) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(requiredMessage), field.getId());
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            mandatory.addEnablingConditions(visibilityConditions);
            mandatory.addEnablingConditions(
                    collectConditions(
                            source,
                            N2oField.RequiringDependency.class,
                            N2oField.VisibilityDependency.class
                    )
            );
            if (mandatory.getEnablingConditions() != null && !mandatory.getEnablingConditions().isEmpty()) {
                result.add(mandatory);
            }
        }
        return result;
    }

    private List<Validation> initInlineValidations(Field field,
                                       S source,
                                       CompileContext<?, ?> context, CompileProcessor p,
                                       Set<String> visibilityConditions) {

        List<Validation> result = new ArrayList<>();
        N2oField.Validations validations = source.getValidations();
        if (validations == null) return result;
        if (validations.getWhiteList() != null) {
            for (String validation : validations.getWhiteList()) {
                Validation compiledValidation = initWhiteListValidation(field.getId(),
                        validation,
                        source, p,
                        visibilityConditions);
                if (compiledValidation != null)
                    result.add(compiledValidation);
            }
        }
        if (validations.getInlineValidations() != null) {
            List<String> fieldVisibilityConditions = new ArrayList<>();
            if (source.getDependencies() != null) {
                for (N2oField.Dependency dependency : source.getDependencies()) {
                    if (dependency.getClass().equals(N2oField.VisibilityDependency.class))
                        fieldVisibilityConditions.add(dependency.getValue());
                }
            }
            for (N2oValidation v : validations.getInlineValidations()) {
                v.setFieldId(field.getId());
                Validation compiledValidation = p.compile(v, context);
                MomentScope momentScope = p.getScope(MomentScope.class);
                if (momentScope != null)
                    compiledValidation.setMoment(momentScope.getMoment());
                if (!fieldVisibilityConditions.isEmpty()) {
                    compiledValidation.addEnablingConditions(fieldVisibilityConditions);
                }
                compiledValidation.addEnablingConditions(visibilityConditions);
                result.add(compiledValidation);
            }
        }
        return result;
    }

    private Validation initWhiteListValidation(String fieldId,
                                         String refId,
                                         S source,
                                         CompileProcessor p,
                                         Set<String> visibilityConditions) {
        CompiledObject object = p.getScope(CompiledObject.class);
        if (object == null)
            return null;
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
            throw new N2oException(String.format("Field %s contains validation reference for nonexistent validation!", fieldId));
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
            return null;
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
        return validation;
    }

    private List<String> collectConditions(S source, Class... types) {
        List<String> result = new ArrayList<>();
        if (source.getDependencies() != null && types != null) {
            for (N2oField.Dependency dependency : source.getDependencies()) {
                for (Class clazz : types) {
                    if (dependency.getClass().equals(clazz)) {
                        result.add(ScriptProcessor.resolveFunction(dependency.getValue()));
                    }
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    protected void compileCopied(S source, CompileProcessor p) {
        if (Boolean.TRUE.equals(source.getCopied())) {
            CopiedFieldScope scope = p.getScope(CopiedFieldScope.class);
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (scope != null && widgetScope != null) {
                scope.addCopiedFields(source.getId(), widgetScope.getDatasourceId());
            }
        }
    }

    protected void compileDefaultValues(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        UploadScope uploadScope = p.getScope(UploadScope.class);
        WidgetParamScope paramScope = p.getScope(WidgetParamScope.class);
        if (paramScope != null) {
            compileParams(control, source, paramScope, p);
        }

        if (uploadScope != null && !UploadType.defaults.equals(uploadScope.getUpload()) &&
                Boolean.TRUE.equals(source.getCopied()))
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
                    ModelLink defaultValue = getDefaultValueModelLink(source, context, p);
                    if (source.getRefFieldId() == null)
                        defaultValue.setValue(defValue);
                    defaultValue.setParam(source.getParam());
                    defaultValues.add(control.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(control.getId(), p);
                    ModelLink modelLink = getDefaultValueModelLink(source, context, p);
                    if (defValue instanceof DefaultValues) {
                        Map<String, Object> values = ((DefaultValues) defValue).getValues();
                        if (values != null) {
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
                    String param = source.getParam() != null ?
                            source.getParam() :
                            "!!_" + control.getId();//fixme возможно правильнее null
                    modelLink.setParam(param);
                    defaultValues.add(control.getId(), modelLink);
                }
            } else if (source.getRefFieldId() != null) {
                ModelLink modelLink = getDefaultValueModelLink(source, context, p);
                modelLink.setParam(source.getParam());
                defaultValues.add(control.getId(), modelLink);
            }
        }
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

    /**
     * Сборка значений по умолчанию у поля из заданных параметров
     *
     * @param source Исходная модель поля
     * @param p      Процессор сборки
     * @return Значение по умолчанию поля
     */
    protected void compileParams(D control, S source, WidgetParamScope paramScope, CompileProcessor p) {
        if (source.getParam() != null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (widgetScope != null) {
                ModelLink onSet = new ModelLink(widgetScope.getModel(), widgetScope.getGlobalDatasourceId(), control.getId());
                onSet.setParam(source.getParam());
                ReduxAction onGet = Redux.dispatchUpdateModel(widgetScope.getGlobalDatasourceId(), widgetScope.getModel(), control.getId(),
                        colon(source.getParam()));
                paramScope.addQueryMapping(source.getParam(), onGet, onSet);
            }
        }
    }

    /**
     * Получение модели для дефолтного значения поля
     *
     * @param source Исходная модель поля
     * @param p      Процессор сборки метаданных
     * @return Модель для дефолтного значения поля
     */
    private ModelLink getDefaultValueModelLink(S source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String globalDatasourceId = null;
        switch (source.getRefPage()) {
            case THIS:
                globalDatasourceId = pageScope != null ? pageScope.getClientDatasourceId(source.getRefDatasource()) : source.getRefDatasource();
                break;
            case PARENT:
                if (context instanceof PageContext) {
                    globalDatasourceId = CompileUtil.generateDatasourceId(((PageContext)context).getParentClientPageId(), source.getRefDatasource());
                } else
                    throw new N2oException(String.format("Field %s has ref-page=\"parent\" but PageContext not found", source.getId()));
        }
        ModelLink defaultValue;
        if (source.getRefFieldId() != null) {
            defaultValue = new ModelLink(source.getRefModel(), globalDatasourceId, source.getRefFieldId());
        } else {
            defaultValue = new ModelLink(source.getRefModel(), globalDatasourceId);
            defaultValue.setValue(p.resolveJS(source.getDefaultValue()));
        }

        if (N2oField.Page.THIS.equals(source.getRefPage()))
            defaultValue.setObserve(true);

        return defaultValue;
    }

    protected String initLocalDatasourceId(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
            if (datasourceIdAware != null) {
                return datasourceIdAware.getDatasource();
            }
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }
}
