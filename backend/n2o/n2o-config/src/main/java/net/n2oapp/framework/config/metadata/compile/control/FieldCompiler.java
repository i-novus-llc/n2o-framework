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
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
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
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetVisibilityScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.util.CompileUtil;
import net.n2oapp.framework.config.util.ControlFilterUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.QueryContextUtil.prepareQueryContextForRouteRegister;

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

        field.setId(source.getId());

        compileCondition(source, source::getVisible, new N2oField.VisibilityDependency(), field::setVisible, !"false".equals(source.getVisible()));
        compileCondition(source, source::getEnabled, new N2oField.EnablingDependency(), field::setEnabled, !"false".equals(source.getEnabled()));
        compileCondition(source, source::getRequired, new N2oField.RequiringDependency(), field::setRequired, "true".equals(source.getRequired()));

        compileFieldToolbar(field, source, context, p);
        field.setLabel(initLabel(source, p));
        field.setNoLabelBlock(p.cast(source.getNoLabelBlock(),
                p.resolve(property("n2o.api.field.no_label_block"), Boolean.class)));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        compileDependencies(field, source, context, p);
    }

    private void compileCondition(S source, Supplier<String> conditionGetter, N2oField.Dependency dependency,
                                  Consumer<Boolean> conditionSetter, Boolean defaultValue) {
        if (StringUtils.isLink(conditionGetter.get())) {
            conditionSetter.accept(false);
            Set<String> onFields = ScriptProcessor.extractVars(conditionGetter.get());
            dependency.setValue(StringUtils.unwrapLink(conditionGetter.get()));
            dependency.setOn(onFields.toArray(String[]::new));
            source.addDependency(dependency);
        } else if (conditionGetter.get() != null) {
            conditionSetter.accept(defaultValue);
            dependency.setValue(conditionGetter.get());
            source.addDependency(dependency);
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

    private ControlDependency compileControlDependency(Field field, N2oField.Dependency d, CompileProcessor p) {
        ControlDependency dependency = new ControlDependency();
        if (d instanceof N2oField.EnablingDependency)
            dependency.setType(ValidationType.enabled);
        else if (d instanceof N2oField.RequiringDependency)
            dependency.setType(ValidationType.required);
        else if (d instanceof N2oField.VisibilityDependency) {
            dependency.setType(ValidationType.visible);
            Boolean isResettable = p.cast(((N2oField.VisibilityDependency) d).getReset(),
                    p.resolve(property("n2o.api.control.visibility.auto_reset"), Boolean.class));
            if (Boolean.TRUE.equals(isResettable)) {
                ControlDependency reset = new ControlDependency();
                reset.setType(ValidationType.reset);
                reset.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
                addToField(reset, field, d, p);
            }
        } else if (d instanceof N2oField.SetValueDependency)
            dependency.setType(ValidationType.setValue);
        else if (d instanceof N2oField.FetchDependency)
            dependency.setType(ValidationType.fetch);
        else if (d instanceof N2oField.ResetDependency) {
            dependency.setType(ValidationType.reset);
            if (d.getValue() == null) {
                d.setValue(String.valueOf(Boolean.TRUE));
            }
        }
        dependency.setExpression(ScriptProcessor.resolveFunction(d.getValue()));
        return dependency;
    }

    private FetchValueDependency compileFetchDependency(N2oField.Dependency d, CompileContext<?, ?> context, CompileProcessor p) {
        FetchValueDependency dependency = new FetchValueDependency();
        dependency.setType(ValidationType.fetchValue);
        dependency.setValueFieldId(p.cast(((N2oField.FetchValueDependency) d).getValueFieldId(), "name"));
        dependency.setDataProvider(compileFetchDependencyDataProvider((N2oField.FetchValueDependency) d, context, p));
        return dependency;
    }

    private ClientDataProvider compileFetchDependencyDataProvider(N2oField.FetchValueDependency field, CompileContext<?, ?> context, CompileProcessor p) {
        QueryContext queryContext = new QueryContext(field.getQueryId());
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        queryContext.setFailAlertWidgetId(modelsScope != null ? modelsScope.getWidgetId() : null);
        CompiledQuery query = p.getCompiled(queryContext);
        p.addRoute(prepareQueryContextForRouteRegister(query));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        if (modelsScope != null) {
            dataProvider.setTargetModel(modelsScope.getModel());
            dataProvider.setTargetWidgetId(modelsScope.getWidgetId());
        }
        dataProvider.setUrl(query.getRoute());
        dataProvider.setSize(field.getSize());

        N2oPreFilter[] preFilters = field.getPreFilters();
        if (preFilters != null) {
            N2oParam[] queryParams = new N2oParam[preFilters.length];
            for (int i = 0; i < preFilters.length; i++) {
                N2oPreFilter preFilter = preFilters[i];
                N2oQuery.Filter filter = query.getFilterByPreFilter(preFilter);
                N2oParam queryParam = new N2oParam();
                queryParam.setName(query.getFilterIdToParamMap().get(filter.getFilterField()));
                if (preFilter.getParam() == null) {
                    queryParam.setValueList(getPrefilterValue(preFilter));
                    queryParam.setRefModel(preFilter.getRefModel());
                    queryParam.setRefWidgetId(preFilter.getRefWidgetId());
                } else {
                    queryParam.setValueParam(preFilter.getParam());
                }
                queryParams[i] = queryParam;
            }
            dataProvider.setQueryParams(queryParams);
        }
        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }

    protected void compileFilters(S source, CompileProcessor p) {
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
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
                ModelLink link = new ModelLink(ReduxModel.FILTER, widgetScope.getClientWidgetId());
                link.setSubModelQuery(subModelQuery);
                link.setValue(p.resolveJS(Placeholders.ref(f.getFilterField())));
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
        if (subModelsScope != null) {
            return subModelsScope.stream()
                    .filter(subModelQuery -> fieldId.equals(subModelQuery.getSubModel()))
                    .findAny()
                    .orElse(null);
        }
        return null;
    }

    protected void initValidations(S source, Field field, CompileContext<?, ?> context, CompileProcessor p) {
        List<Validation> serverValidations = new ArrayList<>();
        List<Validation> clientValidations = new ArrayList<>();
        Set<String> visibilityConditions = p.getScope(FieldSetVisibilityScope.class);
        MomentScope momentScope = p.getScope(MomentScope.class);
        String REQUIRED_MESSAGE = momentScope != null
                && N2oValidation.ServerMoment.beforeQuery.equals(momentScope.getMoment())
                ? "n2o.required.filter" : "n2o.required.field";
        if ("true".equals(source.getRequired())) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(REQUIRED_MESSAGE), field.getId());
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            mandatory.addEnablingConditions(collectConditions(source, N2oField.VisibilityDependency.class));
            mandatory.addEnablingConditions(visibilityConditions);
            serverValidations.add(mandatory);
            clientValidations.add(mandatory);
            field.setRequired(true);
        } else if (source.containsDependency(N2oField.RequiringDependency.class)) {
            MandatoryValidation mandatory = new MandatoryValidation(source.getId(), p.getMessage(REQUIRED_MESSAGE), field.getId());
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
            mandatory.setEnablingExpression(ScriptProcessor.resolveFunction(
                    ScriptProcessor.and(collectConditions(source, N2oField.RequiringDependency.class))));
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
            ArrayList<Validation> objectValidations = new ArrayList<>();
            for (N2oValidation v : validations.getInlineValidations()) {
                v.setFieldId(field.getId());
                Validation compiledValidation = p.compile(v, context);
                MomentScope momentScope = p.getScope(MomentScope.class);
                if (momentScope != null)
                    compiledValidation.setMoment(momentScope.getMoment());
                if ("false".equals(source.getVisible())) {
                    continue;
                } else if (!enablingConditions.isEmpty()) {
                    compiledValidation.addEnablingConditions(enablingConditions);
                }
                compiledValidation.addEnablingConditions(visibilityConditions);
                objectValidations.add(compiledValidation);
                serverValidations.add(compiledValidation);
                if (compiledValidation.getSide() == null || compiledValidation.getSide().contains("client"))
                    clientValidations.add(compiledValidation);
            }
            if (object != null)
                object.getValidations().addAll(objectValidations);
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

    private List<String> collectConditions(S source, Class... types) {
        List<String> result = new ArrayList<>();
        if (source.getDependencies() != null && types != null) {
            for (N2oField.Dependency dependency : source.getDependencies()) {
                for (Class clazz : types) {
                    if (dependency.getClass().equals(clazz)) {
                        result.add(dependency.getValue());
                    }
                }
            }
        }
        return result.isEmpty() ? null : result;
    }

    protected void compileCopied(S source, CompileProcessor p) {
        if (Boolean.TRUE.equals(source.getCopied())) {
            CopiedFieldScope scope = p.getScope(CopiedFieldScope.class);
            if (scope != null) {
                scope.addCopiedFields(source.getId());
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
                    ModelLink defaultValue = getModelLink(control, source, context, p, defaultValues);
                    if (source.getRefFieldId() == null)
                        defaultValue.setValue(defValue);
                    defaultValue.setParam(source.getParam());
                    defaultValues.add(control.getId(), defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(control.getId(), p);
                    ModelLink modelLink = getModelLink(control, source, context, p, defaultValues);
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
                    modelLink.setParam(source.getParam());
                    defaultValues.add(control.getId(), modelLink);
                }
            } else {
                if (source.getRefPage() != null || source.getRefWidgetId() != null || source.getRefFieldId() != null) {
                    ModelLink modelLink = getModelLink(control, source, context, p, defaultValues);
                    modelLink.setParam(source.getParam());
                    defaultValues.add(control.getId(), modelLink);
                }
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
            ModelsScope modelsScope = p.getScope(ModelsScope.class);
            if (modelsScope != null) {
                ModelLink onSet = new ModelLink(modelsScope.getModel(), modelsScope.getWidgetId(), control.getId());
                onSet.setParam(source.getParam());
                ReduxAction onGet = Redux.dispatchUpdateModel(modelsScope.getWidgetId(), modelsScope.getModel(), control.getId(),
                        colon(source.getParam()));
                paramScope.addQueryMapping(source.getParam(), onGet, onSet);
                if (modelsScope.hasModels())
                    modelsScope.add(control.getId(), onSet);
            }
        }
    }

    private ModelLink getModelLink(D control, S source, CompileContext<?, ?> context, CompileProcessor p, ModelsScope defaultValues) {
        ModelLink defaultValue;
        if (N2oField.Page.PARENT.equals(source.getRefPage())) {
            if (context instanceof PageContext) {
                String widgetId = source.getRefWidgetId() == null ?
                        ((PageContext) context).getParentClientWidgetId() :
                        CompileUtil.generateWidgetId(((PageContext) context).getParentClientPageId(),
                                source.getRefWidgetId());
                defaultValue = new ModelLink(p.cast(source.getRefModel(), defaultValues.getModel(), ReduxModel.RESOLVE),
                        widgetId, source.getRefFieldId());
            } else {
                throw new N2oException(String.format("Field %s has ref-page=\"parent\" but PageContext not found",
                        control.getId()));
            }
        } else {
            String widgetId = source.getRefWidgetId() == null ? defaultValues.getWidgetId()
                    : CompileUtil.generateWidgetId(((PageContext) context).getClientPageId(), source.getRefWidgetId());
            defaultValue = new ModelLink(p.cast(source.getRefModel(), defaultValues.getModel()), widgetId, source.getRefFieldId());
        }
        return defaultValue;
    }
}
