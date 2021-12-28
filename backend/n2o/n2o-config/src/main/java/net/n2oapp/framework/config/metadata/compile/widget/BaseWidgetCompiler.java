package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDependency;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.util.CompileUtil;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.*;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция абстрактного виджета
 */
public abstract class BaseWidgetCompiler<D extends Widget, S extends N2oWidget> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected abstract String getPropertyWidgetSrc();

    /**
     * Базовая сборка виджета
     */
    protected void compileBaseWidget(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                     CompiledObject object) {
        compiled.setId(initGlobalWidgetId(source, context, p));
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAttributes(source));
        compiled.setName(p.cast(source.getName(), object != null ? object.getName() : null, source.getId()));
        compiled.setSrc(initSrc(source, p));
        compiled.setIcon(source.getIcon());
        compileAutoFocus(source, compiled, p);
        if (compiled.getComponent() != null) {
            compiled.getComponent().setAutoFocus(initAutoFocus(source, p));
            compiled.getComponent().setFetchOnInit(initFetchOnInit(source, compiled));
        }
        compileDependencies(compiled, source, p);
    }

    private Boolean initAutoFocus(S source, CompileProcessor p) {
        return p.cast(source.getAutoFocus(), p.resolve(property("n2o.api.widget.auto_focus"), Boolean.class), true);
    }

    private String initSrc(S source, CompileProcessor p) {
        String defaultWidgetSrc = null;
        if (getPropertyWidgetSrc() != null)
            defaultWidgetSrc = p.resolve(property(getPropertyWidgetSrc()), String.class);
        String src = p.cast(source.getSrc(), defaultWidgetSrc);
        return src;
    }

    protected MetaActions initMetaActions(S source) {
        MetaActions metaActions = new MetaActions();
        if (source.getActions() != null) {
            for (ActionsBar actionsBar : source.getActions()) {
                metaActions.addAction(actionsBar.getId(), actionsBar);
            }
        }
        return metaActions;
    }

//    /**
//     * Компиляция идентификатора выборки виджета, учитывая источник данных
//     */
//    private void compileWidgetQueryId(S source, D compiled) {
//        if (source.getUpload() == null) {
//            if (source.getQueryId() != null) {
//                compiled.setQueryId(source.getQueryId());
//                compiled.setUpload(UploadType.query);
//            } else {
//                compiled.setQueryId(source.getDefaultValuesQueryId());
//                compiled.setUpload(UploadType.defaults);
//            }
//        } else {
//            String queryId = null;
//            switch (source.getUpload()) {
//                case query:
//                    if (source.getQueryId() == null)
//                        throw new N2oException("Upload is 'query', but queryId isn't set in widget");
//                    queryId = source.getQueryId();
//                    break;
//                case copy:
//                    queryId = source.getQueryId();
//                    break;
//                case defaults:
//                    queryId = source.getDefaultValuesQueryId();
//            }
//            compiled.setUpload(source.getUpload());
//            compiled.setQueryId(queryId);
//        }
//    }


    /**
     * Инициализация встроенного источника данных
     */
    protected N2oDatasource initInlineDatasource(D compiled, S source, CompileProcessor p) {
        String datasourceId = source.getDatasourceId();
        N2oDatasource datasource;
        if (source.getDatasourceId() == null) {
            datasourceId = CompileUtil.generateSourceDatasourceId(source.getId());
            if (source.getDatasource() == null) {
                datasource = new N2oDatasource();
                datasource.setDefaultValuesMode(DefaultValuesMode.defaults);
            } else {
                datasource = source.getDatasource();
                source.setDatasource(null);
            }
            datasource.setId(datasourceId);
            source.setDatasourceId(datasourceId);
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            if (dataSourcesScope != null) {
                dataSourcesScope.put(datasourceId, datasource);
            }
        } else {
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            datasource = dataSourcesScope.get(datasourceId);
        }
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null) {
            pageScope.getWidgetIdSourceDatasourceMap().put(source.getId(), datasourceId);
            pageScope.getWidgetIdClientDatasourceMap().put(compiled.getId(), pageScope.getGlobalWidgetId(datasourceId));
        }
        compiled.setDatasource(pageScope != null ? pageScope.getClientDatasourceId(datasourceId) : datasourceId);
        return datasource;
    }

    private String initGlobalWidgetId(S source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null) {
            return pageScope.getGlobalWidgetId(source.getId());
        } else {
            return context.getCompiledId((N2oCompileProcessor) p);
        }
    }

    protected void compileToolbarAndAction(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope, MetaActions widgetActions,
                                           CompiledObject object, ValidationList validationList) {
        actionsToToolbar(source);
        compileToolbar(compiled, source, context, p, object, widgetActions, widgetScope, validationList);
    }

    protected void addParamRoutes(WidgetParamScope paramScope, CompileContext<?, ?> context, CompileProcessor p) {
        if (paramScope != null && !paramScope.getQueryMapping().isEmpty()) {
            PageRoutes routes = p.getScope(PageRoutes.class);
            Models models = p.getScope(Models.class);
            paramScope.getQueryMapping().forEach((k, v) -> {
                if (context.getPathRouteMapping() == null || !context.getPathRouteMapping().containsKey(k)) {
                    if (routes != null)
                        routes.addQueryMapping(k, v.getOnGet(), v.getOnSet());
                } else {
                    if (models != null && v.getOnSet() instanceof ModelLink) {
                        ModelLink link = (ModelLink) v.getOnSet();
                        models.add(link, link);
                    }
                }
            });
        }
    }

    private boolean initFetchOnInit(S source, D compiled) {
        boolean fetchOnInit;
        if (source.getFetchOnInit() != null)
            fetchOnInit = source.getFetchOnInit();
        else
            fetchOnInit = compiled.getDatasource() != null;
        return fetchOnInit;
    }

    private void compileAutoFocus(S source, D compiled, CompileProcessor p) {
        if (compiled.getComponent() == null)
            return;
        compiled.getComponent().setAutoFocus(p.cast(source.getAutoFocus(), p.resolve(property("n2o.api.widget.auto_focus"), Boolean.class), true));
    }

    private void actionsToToolbar(S source) {
        if (source.getActions() == null || source.getToolbars() == null)
            return;
        Map<String, ActionsBar> actionMap = new HashMap<>();
        Stream.of(source.getActions()).forEach(a -> actionMap.put(a.getId(), a));
        for (N2oToolbar toolbar : source.getToolbars()) {
            if (toolbar.getItems() == null) continue;
            ToolbarItem[] toolbarItems = toolbar.getItems();
            copyActionForToolbarItem(actionMap, toolbarItems);
        }
    }

    private void copyActionForToolbarItem(Map<String, ActionsBar> actionMap, ToolbarItem[] toolbarItems) {
        for (ToolbarItem item : toolbarItems) {
            if (item instanceof N2oButton) {
                copyAction((N2oButton) item, actionMap);
            } else if (item instanceof N2oSubmenu) {
                for (N2oButton subItem : ((N2oSubmenu) item).getMenuItems()) {
                    copyAction(subItem, actionMap);
                }
            } else if (item instanceof N2oGroup) {
                copyActionForToolbarItem(actionMap, ((N2oGroup) item).getItems());
            }
        }
    }

    private void copyAction(N2oButton item, Map<String, ActionsBar> actionMap) {
        N2oButton mi = item;
        if (mi.getAction() == null && mi.getActionId() != null) {
            ActionsBar action = actionMap.get(mi.getActionId());
            if (action == null) {
                throw new N2oException(String.format("Toolbar has reference to nonexistent action by actionId %s!", mi.getAction()));
            }
            mi.setAction(action.getAction());//todo скорее всего не нужно
            if (mi.getModel() == null)
                mi.setModel(action.getModel());
            if (mi.getWidgetId() == null)
                mi.setWidgetId(action.getWidgetId());
            if (mi.getLabel() == null)
                mi.setLabel(action.getLabel());
            if (mi.getIcon() == null)
                mi.setIcon(action.getIcon());
        }
    }

    private void compileToolbar(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p, Object... scopes) {
        if (source.getToolbars() == null)
            return;

        Toolbar compiledToolbar = new Toolbar();
        IndexScope index = new IndexScope();
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.widget.toolbar.place"), String.class));
        for (N2oToolbar toolbar : source.getToolbars()) {
            compiledToolbar.putAll(p.compile(toolbar, context,
                    index, toolbarPlaceScope, scopes));
        }
        compiled.setToolbar(compiledToolbar);
    }

    /**
     * Получить собранный объект виджета
     */
    protected CompiledObject getObject(S source, N2oDatasource datasource, CompileProcessor p) {
        if (datasource != null) {
            PageScope pageScope = p.getScope(PageScope.class);
            if (datasource.getObjectId() == null) {
                if (datasource.getQueryId() == null) {
                    if (pageScope != null && pageScope.getResultWidgetId() != null &&
                            source.getId().equals(pageScope.getResultWidgetId()) && pageScope.getObjectId() != null) {
                        return p.getCompiled(new ObjectContext(pageScope.getObjectId()));
                    }
                } else {
                    CompiledQuery query = p.getCompiled(new QueryContext(datasource.getQueryId()));
                    if (pageScope != null && pageScope.getResultWidgetId() != null &&
                            source.getId().equals(pageScope.getResultWidgetId()) && pageScope.getObjectId() != null &&
                            !query.getObject().getId().equals(pageScope.getObjectId()))
                        throw new IllegalArgumentException("object-id for main widget must be equal object-id in page");
                    return query.getObject();
                }
            } else {
                if (pageScope != null && pageScope.getResultWidgetId() != null &&
                        source.getId().equals(pageScope.getResultWidgetId()) && pageScope.getObjectId() != null &&
                        !datasource.getObjectId().equals(pageScope.getObjectId()))
                    throw new IllegalArgumentException("object-id for main widget must be equal object-id in page");
                return p.getCompiled(new ObjectContext(datasource.getObjectId()));
            }
        }
        return null;
    }

    private void compileDependencies(D compiled, S source, CompileProcessor p) {
        WidgetDependency dependency = new WidgetDependency();
        if (source.getVisible() != null) {
            Object condition = p.resolveJS(source.getVisible(), Boolean.class);
            if (StringUtils.isJs(condition)) {
                DependencyCondition visibilityCondition = new DependencyCondition();
                List<DependencyCondition> visible = new ArrayList<>();
                visibilityCondition.setCondition(((String) condition).substring(1, ((String) condition).length() - 1));
                visible.add(visibilityCondition);
                dependency.setVisible(visible);
            } else if (condition instanceof Boolean) {
                compiled.setVisible((Boolean) condition);
            }
        }
        if (!dependency.isEmpty()) {
            compiled.setDependency(dependency);
        }
    }

//    /**
//     * Инициализация выборки виджета по id
//     */
//    private CompiledQuery getDataProviderQuery(String queryId, CompileProcessor p) {
//        return queryId == null ? null : p.getCompiled(new QueryContext(queryId));
//    }

    /**
     * Получить собранную выборку виджета
     */
    protected CompiledQuery getQuery(S source, N2oDatasource datasource, CompileProcessor p) {
        if (datasource != null) {
            return datasource.getQueryId() != null ? p.getCompiled(new QueryContext(datasource.getQueryId())) : null;
        }
        return null;
    }

    protected FieldSetScope initFieldSetScope(CompiledQuery query) {
        FieldSetScope scope = new FieldSetScope();
        if (query != null) {
            Map<String, N2oQuery.Field> fieldsMap = query.getFieldsMap();
            for (Map.Entry<String, N2oQuery.Field> entry : fieldsMap.entrySet()) {
                if (entry.getValue() != null) {
                    scope.put(entry.getKey(), entry.getValue().getName());
                }
            }
        }
        return scope;
    }

    /**
     * Инициализация филдсетов
     *
     * @param fields      Список полей или филдсетов или строк или столбцов
     * @param context     Контекст сборки
     * @param widgetQuery Выборка виджета
     * @param p           Процессор сборки
     * @return Список филдсетов
     */
    protected List<FieldSet> initFieldSets(SourceComponent[] fields, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope,
                                           CompiledQuery widgetQuery,
                                           Object... scopes) {
        if (fields == null)
            return Collections.emptyList();
        FieldSetScope fieldSetScope = initFieldSetScope(widgetQuery);
        IndexScope indexScope = new IndexScope();
        List<FieldSet> fieldSets = new ArrayList<>();
        int i = 0;
        while (i < fields.length) {
            N2oFieldSet fieldSet;
            if (fields[i] instanceof N2oFieldSet) {
                fieldSet = (N2oFieldSet) fields[i];
                i++;
            } else {
                N2oSetFieldSet newFieldset = new N2oSetFieldSet();
                List<SourceComponent> newFieldsetItems = new ArrayList<>();
                while (i < fields.length && !(fields[i] instanceof N2oFieldSet)) {
                    newFieldsetItems.add(fields[i]);
                    i++;
                }
                SourceComponent[] items = new SourceComponent[newFieldsetItems.size()];
                newFieldset.setItems(newFieldsetItems.toArray(items));
                fieldSet = newFieldset;
            }
            fieldSets.add(p.compile(fieldSet, context,
                    widgetQuery, widgetScope, fieldSetScope, indexScope, scopes));
        }
        return fieldSets;
    }

//    private void initFilters(D compiled, S source, CompileProcessor p) {
//        CompiledQuery query = getDataProviderQuery(compiled.getQueryId(), p);
//        if (query == null)
//            return;
//        List<Filter> filters = new ArrayList<>();
//        String masterWidgetId = null;
//        WidgetScope widgetScope = p.getScope(WidgetScope.class);
//        PageScope pageScope = p.getScope(PageScope.class);
//        if (widgetScope != null) {
//            masterWidgetId = widgetScope.getDependsOnWidgetId();
//        }
//        if (source.getPreFilters() != null) {
//            for (N2oPreFilter preFilter : source.getPreFilters()) {
//                N2oQuery.Filter queryFilter = query.getFilterByPreFilter(preFilter);
//                if (queryFilter != null) {
//                    Filter filter = new Filter();
//                    if (preFilter.getRequired() != null && preFilter.getRequired()) {
//                        if (p.getScope(ValidationList.class) != null) {
//                            MandatoryValidation v = new MandatoryValidation(
//                                    queryFilter.getFilterField(),
//                                    p.getMessage("n2o.required.filter"),
//                                    queryFilter.getFilterField()
//                            );
//                            v.setMoment(N2oValidation.ServerMoment.beforeQuery);
//                            v.setSeverity(SeverityType.danger);
//
//                            if (p.getScope(ValidationList.class).get(compiled.getId(), ReduxModel.FILTER) == null) {
//                                Map<String, List<Validation>> map = new HashMap<>();
//                                map.put(compiled.getId(), new ArrayList<>());
//                                p.getScope(ValidationList.class).getValidations().put(ReduxModel.FILTER, map);
//                            }
//                            List<Validation> validationList = p.getScope(ValidationList.class)
//                                    .get(compiled.getId(), ReduxModel.FILTER);
//                            validationList.add(v);
//                        }
//                    }
//                    filter.setParam(p.cast(preFilter.getParam(), compiled.getId() + "_" + queryFilter.getParam()));
//                    filter.setRoutable(p.cast(preFilter.getRoutable(), false));
//                    filter.setFilterId(queryFilter.getFilterField());
//                    Object prefilterValue = getPrefilterValue(preFilter);
//                    ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
//                    if (routeScope != null && routeScope.getQueryMapping() != null && routeScope.getQueryMapping().containsKey(filter.getParam())) {
//                        //фильтр из родительского маршрута
//                        filter.setLink(routeScope.getQueryMapping().get(filter.getParam()));
//                    } else if (StringUtils.isJs(prefilterValue)) {
//                        String refWidgetId = masterWidgetId;
//                        if (preFilter.getRefWidgetId() != null) {
//                            refWidgetId = preFilter.getRefPageId() == null ?
//                                    pageScope.getGlobalWidgetId(preFilter.getRefWidgetId())
//                                    : CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId());
//                        }
//                        String datasource = pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null
//                                ? refWidgetId : pageScope.getWidgetIdClientDatasourceMap().get(refWidgetId);
//
//                        ReduxModel model = p.cast(preFilter.getModel(), ReduxModel.RESOLVE);
//                        ModelLink link = new ModelLink(model, datasource);
//                        link.setValue(prefilterValue);
//                        link.setParam(filter.getParam());
//                        filter.setLink(link);
//                    } else {
//                        //фильтр с константным значением или значением из параметра в url
//                        ModelLink link = new ModelLink(prefilterValue);
//                        link.setParam(filter.getParam());
//                        filter.setLink(link);
//                    }
//                    filters.add(filter);
//                } else {
//                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
//                }
//            }
//        }
//        compiled.setFilters(filters);
//    }

//    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
//        if (n2oPreFilter.getValues() == null) {
//            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
//        } else {
//            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
//        }
//    }
}
