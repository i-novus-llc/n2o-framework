package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oFilterColumn;
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
import net.n2oapp.framework.api.script.ScriptParserException;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetVisibilityScope;
import net.n2oapp.framework.config.metadata.compile.fieldset.MultiFieldSetScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.util.FieldCompileUtil;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static net.n2oapp.framework.api.StringUtils.isBoolean;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class FieldCompiler<D extends Field, S extends N2oField> extends ComponentCompiler<D, S, CompileContext<?, ?>> {

    private static final String FALSE = "false";
    private static final Pattern EXT_EXPRESSION_PATTERN = Pattern.compile(".*\\(.*\\).*");

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.src";
    }

    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setNoLabel(castDefault(source.getNoLabel(),
                () -> p.resolve(property("n2o.api.control.no_label"), String.class)));
        source.setNoLabelBlock(castDefault(source.getNoLabelBlock(),
                () -> p.resolve(property("n2o.api.control.no_label_block"), String.class)));
        source.setRefPage(castDefault(source.getRefPage(), PageRefEnum.THIS));
        source.setRefDatasourceId(castDefault(source.getRefDatasourceId(), () -> {
            if (source.getRefPage().equals(PageRefEnum.THIS)) {
                return initLocalDatasourceId(p);
            } else if (source.getRefPage().equals(PageRefEnum.PARENT)) {
                if (context instanceof PageContext pageContext) {
                    return pageContext.getParentLocalDatasourceId();
                } else {
                    throw new N2oException(String.format("Поле '%s' имеет атрибут 'ref-page=parent', но PageContext не найден", source.getId()));
                }
            }
            return null;
        }));
        source.setRefModel(castDefault(source.getRefModel(),
                () -> Optional.ofNullable(p.getScope(WidgetScope.class)).map(WidgetScope::getModel).orElse(null),
                () -> ReduxModelEnum.RESOLVE));
        initCondition(source, source::getVisible, new N2oField.VisibilityDependency(), b -> source.setVisible(b.toString()), !FALSE.equals(source.getVisible()));
        initCondition(source, source::getEnabled, new N2oField.EnablingDependency(), b -> source.setEnabled(b.toString()), !FALSE.equals(source.getEnabled()));
        initCondition(source, source::getRequired, new N2oField.RequiringDependency(), b -> source.setRequired(b.toString()), "true".equals(source.getRequired()));
    }

    protected void compileField(D field, S source, CompileContext<?, ?> context, CompileProcessor p) {
        compileComponent(field, source, p);

        IndexScope idx = p.getScope(IndexScope.class);
        field.setId(castDefault(source.getId(), () -> "f" + idx.get()));
        field.setLabel(initLabel(source, p));
        field.setNoLabel(isBoolean(source.getNoLabel())
                ? Boolean.valueOf(source.getNoLabel())
                : p.resolveJS(source.getNoLabel()));
        field.setNoLabelBlock(isBoolean(source.getNoLabelBlock())
                ? Boolean.valueOf(source.getNoLabelBlock())
                : p.resolveJS(source.getNoLabelBlock()));
        field.setLabelClass(p.resolveJS(source.getLabelClass()));
        field.setHelp(p.resolveJS(source.getHelp()));
        field.setDescription(p.resolveJS(source.getDescription()));
        field.setClassName(p.resolveJS(source.getCssClass()));
        field.setRequired(p.resolve(source.getRequired(), Boolean.class));
        field.setVisible(p.resolve(source.getVisible(), Boolean.class));
        field.setEnabled(p.resolve(source.getEnabled(), Boolean.class));
        compileFieldToolbar(field, source, context, p);
        compileDependencies(field, source, context, p);
    }

    private void initCondition(S source, Supplier<String> conditionGetter, N2oField.Dependency dependency,
                               Consumer<Boolean> conditionSetter, Boolean defaultValue) {
        if (StringUtils.isLink(conditionGetter.get())) {
            try {
                Set<String> onFields = ScriptProcessor.extractVars(conditionGetter.get());
                dependency.setOn(onFields.toArray(String[]::new));
            } catch (ScriptParserException e) {
                throw new N2oException(
                        String.format("Невозможно извлечь переменные из выражения '%s'. Попробуйте использовать зависимость полей " +
                                        "с явным указанием переменных в атрибуте 'on'",
                                StringUtils.unwrapLink(conditionGetter.get())));
            }

            dependency.setValue(StringUtils.unwrapLink(conditionGetter.get()));
            if (dependency instanceof N2oField.RequiringDependency requiringDependency)
                requiringDependency.setValidate(false);
            source.addDependency(dependency);
            conditionSetter.accept(false);
        } else {
            conditionSetter.accept(defaultValue);
        }
    }

    protected String initLabel(S source, CompileProcessor p) {
        if (!"true".equals(source.getNoLabel())) {
            return p.resolveJS(source.getLabel());
        }
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
                ControlDependency dependency = compileControlDependency(field, d, p, context);
                addToField(dependency, field, d, p);
            }
        }

        if (source.getDependsOn() != null) {
            ControlDependency dependency = new ControlDependency();
            List<String> ons = Arrays.asList(source.getDependsOn());
            dependency.setOn(ons);
            dependency.setType(ValidationTypeEnum.RE_RENDER);
            field.addDependency(dependency);
        }
    }

    private void addToField(ControlDependency compiled, Field field, N2oField.Dependency source, CompileProcessor p) {
        compiled.setApplyOnInit(castDefault(source.getApplyOnInit(),
                () -> p.resolve(property("n2o.api.control.dependency.apply_on_init"), Boolean.class)));
        if (Boolean.TRUE.equals(compiled.getApplyOnInit())) {
            if (source instanceof N2oField.VisibilityDependency)
                field.setVisible(false);
            else if (source instanceof N2oField.EnablingDependency)
                field.setEnabled(false);
        }
        if (source.getOn() != null) {
            List<String> ons = Arrays.asList(source.getOn());
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

    private ControlDependency compileControlDependency(Field field, N2oField.Dependency source, CompileProcessor p, CompileContext<?, ?> context) {
        ControlDependency dependency = new ControlDependency();

        if (source instanceof N2oField.FetchValueDependency fetchValue) {
            FetchValueDependency fetchValueDependency = new FetchValueDependency();
            fetchValueDependency.setType(ValidationTypeEnum.FETCH_VALUE);
            fetchValueDependency.setValueFieldId(fetchValue.getValueFieldId());
            fetchValueDependency.setDataProvider(compileFetchDependencyDataProvider((N2oField.FetchValueDependency) source, context, p));
            return fetchValueDependency;
        } else if (source instanceof N2oField.EnablingDependency enabling) {
            EnablingDependency enablingDependency = new EnablingDependency();
            enablingDependency.setType(ValidationTypeEnum.ENABLED);
            enablingDependency.setMessage(enabling.getMessage());
            dependency = enablingDependency;
        } else if (source instanceof N2oField.RequiringDependency requiring) {
            RequiringDependency requiringDependency = new RequiringDependency();
            requiringDependency.setType(ValidationTypeEnum.REQUIRED);
            requiringDependency.setValidate(castDefault(requiring.getValidate(),
                    () -> p.resolve(property("n2o.api.control.dependency.requiring.validate"), Boolean.class)));
            dependency = requiringDependency;
        } else if (source instanceof N2oField.VisibilityDependency visibility) {
            dependency.setType(ValidationTypeEnum.VISIBLE);
            Boolean isResettable = castDefault(visibility.getReset(),
                    () -> p.resolve(property("n2o.api.control.dependency.visibility.reset"), Boolean.class));
            if (Boolean.TRUE.equals(isResettable)) {
                ControlDependency reset = new ControlDependency();
                reset.setType(ValidationTypeEnum.RESET);
                reset.setExpression(ScriptProcessor.resolveFunction(source.getValue()));
                addToField(reset, field, source, p);
            }
        } else if (source instanceof N2oField.SetValueDependency setValue) {
            SetValueDependency setValueDependency = new SetValueDependency();
            setValueDependency.setType(ValidationTypeEnum.SET_VALUE);
            setValueDependency.setValidate(castDefault(setValue.getValidate(),
                    () -> p.resolve(property("n2o.api.control.dependency.set_value.validate"), Boolean.class)));
            dependency = setValueDependency;
        } else if (source instanceof N2oField.FetchDependency)
            dependency.setType(ValidationTypeEnum.FETCH);
        else if (source instanceof N2oField.ResetDependency reset) {
            ResetDependency resetDependency = new ResetDependency();
            resetDependency.setType(ValidationTypeEnum.RESET);
            resetDependency.setValidate(castDefault(reset.getValidate(),
                    () -> p.resolve(property("n2o.api.control.dependency.reset.validate"), Boolean.class)));
            if (source.getValue() == null) {
                source.setValue(String.valueOf(Boolean.TRUE));
            }
            dependency = resetDependency;
        }
        dependency.setExpression(ScriptProcessor.resolveFunction(source.getValue()));
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
            List<N2oQuery.Filter> filters = FieldCompileUtil.getFilters(source.getId(), query);
            filters.forEach(f -> {
                Filter filter = new Filter();
                filter.setFilterId(f.getFilterId());
                filter.setParam(castDefault(source.getParam(), () -> widgetScope.getWidgetId() + "_" + f.getParam()));
                filter.setRoutable(true);
                SubModelQuery subModelQuery = findSubModelQuery(source.getId(), p);
                ModelLink link = new ModelLink(ReduxModelEnum.FILTER, widgetScope.getClientDatasourceId());
                link.setSubModelQuery(subModelQuery);
                link.setValue(p.resolveJS(Placeholders.ref(f.getFilterId())));
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
            String fullFieldId = getIdWithMultisetPrefix(fieldId, p);
            return subModelsScope.get(widgetScope.getDatasourceId()).stream()
                    .filter(subModelQuery -> fullFieldId.equals(subModelQuery.getFullName()))
                    .findAny()
                    .orElse(null);
        }
        return null;
    }

    protected void initValidations(S source, Field field, CompileContext<?, ?> context, CompileProcessor p) {
        Set<String> visibilityConditions = p.getScope(FieldSetVisibilityScope.class) != null ? p.getScope(FieldSetVisibilityScope.class).getConditions() : Collections.emptySet();
        String fieldId = getIdWithMultisetPrefix(field.getId(), p);
        List<Validation> fieldValidations = initInlineValidations(fieldId, source, context, p, visibilityConditions);
        List<Validation> validations = new ArrayList<>(fieldValidations);
        Optional<Validation> mandatory = fieldValidations.stream().filter(MandatoryValidation.class::isInstance).findFirst();
        if (mandatory.isEmpty()) {
            Validation requiredValidation = initRequiredValidation(fieldId, field, source, p, visibilityConditions);
            if (requiredValidation != null) {
                validations.add(requiredValidation);
            } else {
                ComponentScope componentScope = p.getScope(ComponentScope.class);
                if (componentScope != null) {
                    N2oFilterColumn component = componentScope.unwrap(N2oFilterColumn.class);
                    if (component != null) {
                        validations.forEach(v -> v.addEnablingCondition(String.format("%s || %s === 0", source.getId(), source.getId())));
                    }
                }
            }
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        ValidationScope validationScope = p.getScope(ValidationScope.class);
        if (widgetScope != null && validationScope != null)
            validationScope.addAll(widgetScope.getDatasourceId(), widgetScope.getModel(), validations);
    }

    private Validation initRequiredValidation(String fieldId, Field field, S source, CompileProcessor p, Set<String> visibilityConditions) {
        MomentScope momentScope = p.getScope(MomentScope.class);
        String requiredMessage = momentScope != null
                && N2oValidation.ServerMomentEnum.BEFORE_QUERY.equals(momentScope.getMoment())
                ? "n2o.required.filter" : "n2o.required.field";
        if (source.containsDependency(N2oField.RequiringDependency.class)) {
            MandatoryValidation mandatory = new MandatoryValidation(fieldId, p.getMessage(requiredMessage), fieldId);
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            mandatory.addEnablingConditions(visibilityConditions);
            mandatory.addEnablingConditions(
                    collectConditions(
                            source,
                            N2oField.RequiringDependency.class,
                            N2oField.EnablingDependency.class,
                            N2oField.VisibilityDependency.class
                    )
            );
            if (mandatory.getEnablingConditions() != null && !mandatory.getEnablingConditions().isEmpty()) {
                return mandatory;
            }
        } else if ("true".equals(source.getRequired())) {
            MandatoryValidation mandatory = new MandatoryValidation(fieldId, p.getMessage(requiredMessage), fieldId);
            if (momentScope != null)
                mandatory.setMoment(momentScope.getMoment());
            mandatory.addEnablingConditions(visibilityConditions);
            mandatory.addEnablingConditions(collectConditions(source, N2oField.VisibilityDependency.class, N2oField.EnablingDependency.class));
            field.setRequired(true);
            return mandatory;
        }
        return null;
    }

    private List<Validation> initInlineValidations(String fieldId, S source,
                                                   CompileContext<?, ?> context, CompileProcessor p,
                                                   Set<String> visibilityConditions) {

        List<Validation> result = new ArrayList<>();
        N2oField.Validations validations = source.getValidations();
        if (validations == null) return result;
        if (validations.getWhiteList() != null) {
            for (String validation : validations.getWhiteList()) {
                Validation compiledValidation = initWhiteListValidation(fieldId,
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
                    if (dependency.getClass().equals(N2oField.VisibilityDependency.class) ||
                            dependency.getClass().equals(N2oField.EnablingDependency.class))
                        fieldVisibilityConditions.add(dependency.getValue());
                }
            } else if (FALSE.equals(source.getVisible()) || FALSE.equals(source.getEnabled())) {
                fieldVisibilityConditions.add(FALSE);
            }

            for (N2oValidation v : validations.getInlineValidations()) {
                if (v.getFieldId() == null)
                    v.setFieldId(fieldId);
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
            throw new N2oException(String.format("Поле '%s' содержит ссылку на несуществующую валидацию", fieldId));
        }
        Validation validation = null;
        if (objectValidation instanceof ConstraintValidation constraint) {
            validation = new ConstraintValidation(constraint);
        } else if (objectValidation instanceof ConditionValidation condition) {
            validation = new ConditionValidation(condition);
        } else if (objectValidation instanceof MandatoryValidation mandatory) {
            validation = new MandatoryValidation(mandatory);
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
        WidgetParamScope paramScope = p.getScope(WidgetParamScope.class);
        if (paramScope != null) {
            compileParams(control, source, paramScope, p);
        }

        ModelsScope defaultValues = p.getScope(ModelsScope.class);
        if (defaultValues != null && defaultValues.hasModels()) {
            Object defValue;
            String controlId = getIdWithMultisetPrefix(control.getId(), p);
            if (source.getDefaultValue() != null) {
                defValue = p.resolve(source.getDefaultValue(), source.getDomain());
            } else {
                defValue = compileDefValues(source, p);
            }
            if (defValue != null) {
                if (defValue instanceof String strDefValue) {
                    defValue = ScriptProcessor.resolveExpression(strDefValue);
                }
                if (StringUtils.isJs(defValue)) {
                    ModelLink defaultValue = getDefaultValueModelLink(source, context, p);
                    if (source.getRefFieldId() == null)
                        defaultValue.setValue(defValue);
                    if (isExternalExpression((String) defValue))
                        defaultValue.setObserve(false);
                    defaultValue.setParam(source.getParam());
                    defaultValues.add(controlId, defaultValue);
                } else {
                    SubModelQuery subModelQuery = findSubModelQuery(control.getId(), p);
                    ModelLink modelLink = getDefaultValueModelLink(source, context, p);
                    if (defValue instanceof DefaultValues defVals) {
                        Map<String, Object> values = defVals.getValues();
                        if (values != null) {
                            for (Map.Entry<String, Object> entry : values.entrySet()) {
                                if (entry.getValue() instanceof String strValue) {
                                    Object value = ScriptProcessor.resolveExpression(strValue);
                                    if (value != null)
                                        values.put(entry.getKey(), value);
                                }
                            }
                        }
                    }
                    modelLink.setValue(defValue);
                    modelLink.setSubModelQuery(subModelQuery);
                    modelLink.setParam(source.getParam());
                    defaultValues.add(controlId, modelLink);
                }
            } else if (PageRefEnum.PARENT.equals(source.getRefPage()) || source.getRefFieldId() != null) {
                ModelLink modelLink = getDefaultValueModelLink(source, context, p);
                modelLink.setParam(source.getParam());
                defaultValues.add(controlId, modelLink);
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
     */
    protected void compileParams(D control, S source, WidgetParamScope paramScope, CompileProcessor p) {
        if (source.getParam() != null) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (widgetScope != null) {
                ModelLink onSet = new ModelLink(widgetScope.getModel(), widgetScope.getClientDatasourceId(), control.getId());
                onSet.setParam(source.getParam());
                ReduxAction onGet = Redux.dispatchUpdateModel(widgetScope.getClientDatasourceId(), widgetScope.getModel(), control.getId(),
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
        String clientDatasourceId = null;
        if (Objects.requireNonNull(source.getRefPage()) == PageRefEnum.THIS) {
            clientDatasourceId = getClientDatasourceId(source.getRefDatasourceId(), p);
        } else if (source.getRefPage() == PageRefEnum.PARENT) {
            if (context instanceof PageContext pageContext) {
                clientDatasourceId = getClientDatasourceId(source.getRefDatasourceId(), pageContext.getParentClientPageId(), p);
            } else
                throw new N2oException(String.format("Поле '%s' имеет атрибут 'ref-page=parent', но PageContext не найден", source.getId()));
        }
        ModelLink defaultValue;
        if (source.getRefFieldId() != null) {
            defaultValue = new ModelLink(source.getRefModel(), clientDatasourceId, source.getRefFieldId());
        } else {
            defaultValue = new ModelLink(source.getRefModel(), clientDatasourceId);
            defaultValue.setValue(p.resolveJS(source.getDefaultValue()));
        }

        if (PageRefEnum.THIS.equals(source.getRefPage()))
            defaultValue.setObserve(true);

        return defaultValue;
    }

    private boolean isExternalExpression(String expression) {
        return EXT_EXPRESSION_PATTERN.matcher(expression).find();
    }

    protected String initLocalDatasourceId(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
            if (datasourceIdAware != null) {
                return datasourceIdAware.getDatasourceId();
            }
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }

    private String getIdWithMultisetPrefix(String fieldId, CompileProcessor p) {
        return p.getScope(MultiFieldSetScope.class) == null ? fieldId : p.getScope(MultiFieldSetScope.class).getPathWithIndexes() + "." + fieldId;
    }
}
