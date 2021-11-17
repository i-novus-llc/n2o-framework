package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDependency;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.CompileUtil;
import net.n2oapp.framework.config.util.StylesResolver;

import java.util.*;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalizeParam;

/**
 * Компиляция абстрактного виджета
 */
public abstract class BaseWidgetCompiler<D extends Widget, S extends N2oWidget> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    private static final String SPREAD_OPERATOR = "*.";

    protected abstract String getPropertyWidgetSrc();

    protected void compileWidget(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                 CompiledObject object) {
        compiled.setMasterParam(source.getMasterParam());
        compiled.setId(initGlobalWidgetId(source, context, p));
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAttributes(source));
        compiled.setObjectId(object != null ? object.getId() : null);
        if (p.getScope(WidgetObjectScope.class) != null)
            p.getScope(WidgetObjectScope.class).put(source.getId(), object);
        compileWidgetQueryId(source, compiled);
        compiled.setName(p.cast(source.getName(), object != null ? object.getName() : null, source.getId()));
        compiled.setRoute(initWidgetRoute(source, p));
        compileMasterLink(compiled, p);
        String defaultWidgetSrc = null;
        if (getPropertyWidgetSrc() != null)
            defaultWidgetSrc = p.resolve(property(getPropertyWidgetSrc()), String.class);
        compiled.setSrc(p.cast(source.getSrc(), defaultWidgetSrc));
        compiled.setIcon(source.getIcon());
        compileAutoFocus(source, compiled, p);
        compiled.setProperties(p.mapAttributes(source));
        compileDependencies(compiled, source, p);
        initFilters(compiled, source, p);
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

    /**
     * Компиляция идентификатора выборки виджета, учитывая источник данных
     */
    private void compileWidgetQueryId(S source, D compiled) {
        if (source.getUpload() == null) {
            if (source.getQueryId() != null) {
                compiled.setQueryId(source.getQueryId());
                compiled.setUpload(UploadType.query);
            } else {
                compiled.setQueryId(source.getDefaultValuesQueryId());
                compiled.setUpload(UploadType.defaults);
            }
        } else {
            String queryId = null;
            switch (source.getUpload()) {
                case query:
                    if (source.getQueryId() == null)
                        throw new N2oException("Upload is 'query', but queryId isn't set in widget");
                    queryId = source.getQueryId();
                    break;
                case copy:
                    queryId = source.getQueryId();
                    break;
                case defaults:
                    queryId = source.getDefaultValuesQueryId();
            }
            compiled.setUpload(source.getUpload());
            compiled.setQueryId(queryId);
        }
    }

    /**
     * Инициализация части маршрута виджета
     *
     * @param source Исходный виджет
     * @param p      Процессор сборки
     * @return Часть маршрута виджета
     */
    private String initWidgetRoute(S source, CompileProcessor p) {
        if (source.getRoute() != null) {
            return source.getRoute();
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null && widgetScope.getDependsOnWidgetId() != null &&
                source.getDetailFieldId() != null) {
            //Если есть master/detail зависимость, то для восстановления необходимо в маршруте добавить идентификатор мастер записи
            String selectedId = normalizeParam(p.cast(source.getMasterParam(), widgetScope.getDependsOnWidgetId() + "_id"));
            return normalize(colon(selectedId)) + normalize(source.getId());
        }
        return normalize(source.getId());
    }

    private void initDataProviderMappings(D compiled, ClientDataProvider dataProvider, CompileProcessor p) {
        dataProvider.setPathMapping(new StrictMap<>());
        dataProvider.setQueryMapping(new StrictMap<>());
        if (compiled.getMasterLink() != null) {
            dataProvider.getPathMapping().put(compiled.getMasterParam(), compiled.getMasterLink());
        }
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            dataProvider.getPathMapping().putAll(parentRouteScope.getPathMapping());
            dataProvider.getQueryMapping().putAll(parentRouteScope.getQueryMapping());
        }
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (compiled.getDependency() != null && compiled.getDependency().getFetch() != null) {
            for (DependencyCondition fetch : compiled.getDependency().getFetch()) {
                if (fetch.getGlobalMasterWidgetId() != null) {
                    ParentRouteScope masterRouteScope = pageRoutesScope.get(fetch.getGlobalMasterWidgetId());
                    if (masterRouteScope != null) {
                        masterRouteScope.getPathMapping().forEach(parentRouteScope.getPathMapping()::putIfAbsent);
                        masterRouteScope.getQueryMapping().forEach(parentRouteScope.getQueryMapping()::putIfAbsent);
                    }
                }
            }
        }
        if (compiled.getFilters() != null) {
            ((List<Filter>) compiled.getFilters()).stream().filter(f -> !dataProvider.getPathMapping().containsKey(f.getParam()))
                    .forEach(f -> dataProvider.getQueryMapping().put(f.getParam(), f.getLink()));
        }
    }


    private String getDatasourceRoute(D compiled, S source, CompileProcessor p) {
        String datasource = p.cast(source.getDatasourceId(), source.getId());
        String route = normalize(datasource);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null && widgetScope.getDependsOnWidgetId() != null &&
                source.getDetailFieldId() != null) {
            //Если есть master/detail зависимость, то для восстановления необходимо в маршруте добавить идентификатор мастер записи
            String selectedId = normalizeParam(p.cast(compiled.getMasterParam(), widgetScope.getDependsOnWidgetId() + "_id"));
            route = normalize(colon(selectedId)) + normalize(datasource);
        }
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            return RouteUtil.normalize(parentRouteScope.getUrl() + route);
        } else {
            return route;
        }
    }


    /**
     * Инициализация текущего маршрута виджета при сборке
     *
     * @param context Контекст
     * @param p       Процессор сборки
     * @return Маршрут виджета соединенный с родительским маршрутом
     */
    protected ParentRouteScope initWidgetRouteScope(D compiled, CompileContext<?, ?> context, CompileProcessor p) {
        String route = compiled.getRoute();
        Map<String, ModelLink> additionalPathParams = null;
        Map<String, ModelLink> additionalQueryParams = null;
        if (compiled.getMasterLink() != null) {
            additionalPathParams = new StrictMap<>();
            additionalPathParams.put(compiled.getMasterParam(), compiled.getMasterLink());
        }
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (compiled.getDependency() != null && compiled.getDependency().getFetch() != null) {
            for (DependencyCondition fetch : compiled.getDependency().getFetch()) {
                if (fetch.getGlobalMasterWidgetId() != null) {
                    ParentRouteScope masterRouteScope = pageRoutesScope.get(fetch.getGlobalMasterWidgetId());
                    if (masterRouteScope != null) {
                        masterRouteScope.getPathMapping().forEach(parentRouteScope.getPathMapping()::putIfAbsent);
                        masterRouteScope.getQueryMapping().forEach(parentRouteScope.getQueryMapping()::putIfAbsent);
                    }
                }
            }
        }
        ParentRouteScope widgetRouteScope;
        if (parentRouteScope != null) {
            widgetRouteScope = new ParentRouteScope(route, additionalPathParams, additionalQueryParams, parentRouteScope);
        } else if (context.getRoute((N2oCompileProcessor) p) != null) {
            widgetRouteScope = new ParentRouteScope(context.getRoute((N2oCompileProcessor) p), additionalPathParams, additionalQueryParams);
        } else {
            widgetRouteScope = new ParentRouteScope(route, additionalPathParams, additionalQueryParams);
        }

        return widgetRouteScope;
    }

    private void compileMasterLink(D compiled, CompileProcessor p) {
        String route = compiled.getRoute();
        List<String> params = RouteUtil.getParams(route);
        if (!params.isEmpty()) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (params.size() > 1) {
                throw new N2oException("Widget route can not contain more then one param: " + route);
            }
            if (widgetScope == null || widgetScope.getDependsOnWidgetId() == null) {
                throw new N2oException("Widget route contains params " + route + ", but parent widget not found! May you have forgotten to specify 'depends-on' attribute?");
            }
            String masterIdParam = params.get(0);
            ModelLink masterLink = Redux.linkQuery(widgetScope.getDependsOnWidgetId(), N2oQuery.Field.PK,
                    widgetScope.getDependsOnQueryId());
            compiled.setMasterParam(masterIdParam);
            compiled.setMasterLink(masterLink);
        }
    }

    protected void compileDataProviderAndRoutes(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                                ValidationList validationList, ParentRouteScope widgetRouteScope,
                                                SubModelsScope subModelsScope, CopiedFieldScope copiedFieldScope,
                                                CompiledObject object) {
        compiled.setDataProvider(initDataProvider(compiled, source, context, p, validationList,
                subModelsScope, copiedFieldScope, object));
        compileRouteWidget(compiled, source, getDataProviderQuery(compiled.getQueryId(), p), p, widgetRouteScope);
        compileFetchOnInit(source, compiled);
        PageScope pageScope = p.getScope(PageScope.class);
        compiled.setDatasource(pageScope == null || pageScope.getWidgetIdDatasourceMap() == null
                ? compiled.getId() : pageScope.getWidgetIdDatasourceMap().get(compiled.getId()));
    }

    protected void collectValidation(FieldSet fs, Map<String, List<Validation>> clientValidations, ValidationScope validationScope) {
        if (fs.getRows() == null)
            return;
        fs.getRows().stream().filter(row -> row.getCols() != null).forEach(row -> {
            row.getCols().stream().forEach(col -> {
                if (col.getFields() != null) {
                    col.getFields().forEach(f -> {
                        if (f.getServerValidations() != null) {
                            validationScope.addAll(f.getServerValidations());
                        }
                        if (f.getClientValidations() != null) {
                            clientValidations.put(f.getId(), f.getClientValidations());
                        }
                    });
                }
                if (col.getFieldsets() != null) {
                    col.getFieldsets().forEach(fieldset -> collectValidation(fieldset, clientValidations, validationScope));
                }
            });
        });
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
                                           WidgetScope widgetScope, ParentRouteScope widgetRouteScope, MetaActions widgetActions,
                                           CompiledObject object, ValidationList validationList) {
        actionsToToolbar(source);
        compileToolbar(compiled, source, object, context, p, widgetActions, widgetScope, widgetRouteScope, validationList);
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

    private void compileFetchOnInit(S source, D compiled) {
        if (compiled.getComponent() == null)
            return;

        boolean fetchOnInit;
        if (source.getFetchOnInit() != null)
            fetchOnInit = source.getFetchOnInit();
        else
            fetchOnInit = source.getDependsOn() == null && compiled.getDataProvider() != null;

        compiled.getComponent().setFetchOnInit(fetchOnInit);
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

    private void compileToolbar(D compiled, S source, CompiledObject object, CompileContext<?, ?> context, CompileProcessor p
            , MetaActions compiledActions, WidgetScope widgetScope, ParentRouteScope widgetRouteScope, ValidationList validations) {
        if (source.getToolbars() == null)
            return;

        Toolbar compiledToolbar = new Toolbar();
        IndexScope index = new IndexScope();
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.widget.toolbar.place"), String.class));
        for (N2oToolbar toolbar : source.getToolbars()) {
            compiledToolbar.putAll(p.compile(toolbar, context, widgetScope, widgetRouteScope, compiledActions, object,
                    index, validations, toolbarPlaceScope));
        }
        compiled.setToolbar(compiledToolbar);
    }

    private void compileRouteWidget(D compiled, S source, CompiledQuery query, CompileProcessor p, ParentRouteScope widgetRouteScope) {
        PageRoutes routes = p.getScope(PageRoutes.class);
        if (routes == null)
            return;
        String widgetRoute = widgetRouteScope.getUrl();
        //Регистрация основного маршрута виджета для страницы
        routes.addRoute(widgetRouteScope.getUrl());
        if (compiled.getMasterLink() != null)
            routes.addPathMapping(compiled.getMasterParam(),
                    Redux.dispatchSelectedWidget(compiled.getMasterLink().getDatasource(), colon(compiled.getMasterParam())));

        //Маршрут с выделенной записью в виджете /page/widget/:widget_id
        //todo для формы не существует selected!
        String selectedId = normalizeParam(compiled.getId() + "_id");
        String routeWidgetSelected = normalize(widgetRoute + normalize(colon(selectedId)));
        routes.addRoute(routeWidgetSelected);

        ReduxAction widgetIdMapping = Redux.dispatchSelectedWidget(compiled.getId(), colon(selectedId));
        routes.addPathMapping(selectedId, widgetIdMapping);

        if (query != null) {
            ((List<Filter>) compiled.getFilters()).stream()
                    .filter(Filter::getRoutable)
                    .filter(f -> !f.getLink().isConst())
                    .forEach(filter -> {
                        ReduxAction onGet;
                        String filterId = filter.getFilterId();
                        if (filterId.contains(SPREAD_OPERATOR)) {
                            onGet = Redux.dispatchUpdateMapModel(compiled.getId(), ReduxModel.FILTER,
                                    filterId.substring(0, filterId.indexOf(SPREAD_OPERATOR)),
                                    filterId.substring(filterId.indexOf(SPREAD_OPERATOR) + 2), colon(filter.getParam()));
                        } else {
                            onGet = Redux.dispatchUpdateModel(compiled.getId(), ReduxModel.FILTER, filterId, colon(filter.getParam()));
                        }
                        routes.addQueryMapping(filter.getParam(), onGet, filter.getLink());
                    });
            for (N2oQuery.Field field : query.getSortingFields()) {
                String sortParam = RouteUtil.normalizeParam("sorting." + source.getId() + "_" + field.getId());
                BindLink onSet = Redux.createSortLink(compiled.getId(), field.getId());
                ReduxAction onGet = Redux.dispatchSortWidget(compiled.getId(), field.getId(), colon(sortParam));
                routes.addQueryMapping(sortParam, onGet, onSet);
            }
        }
    }

    protected CompiledObject getObject(S source, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getObjectId() == null) {
            if (source.getQueryId() == null) {
                if (pageScope != null && pageScope.getResultWidgetId() != null &&
                        source.getId().equals(pageScope.getResultWidgetId()) && pageScope.getObjectId() != null) {
                    return p.getCompiled(new ObjectContext(pageScope.getObjectId()));
                }
            } else {
                CompiledQuery query = p.getCompiled(new QueryContext(source.getQueryId()));
                if (pageScope != null && pageScope.getResultWidgetId() != null &&
                        source.getId().equals(pageScope.getResultWidgetId()) && pageScope.getObjectId() != null &&
                        !query.getObject().getId().equals(pageScope.getObjectId()))
                    throw new IllegalArgumentException("object-id for main widget must be equal object-id in page");
                return query.getObject();
            }
        } else {
            if (pageScope != null && pageScope.getResultWidgetId() != null &&
                    source.getId().equals(pageScope.getResultWidgetId()) && pageScope.getObjectId() != null &&
                    !source.getObjectId().equals(pageScope.getObjectId()))
                throw new IllegalArgumentException("object-id for main widget must be equal object-id in page");
            return p.getCompiled(new ObjectContext(source.getObjectId()));
        }
        return null;
    }

    private ClientDataProvider initDataProvider(D widget, S source, CompileContext<?, ?> context,
                                                CompileProcessor p, ValidationList validationList,
                                                SubModelsScope subModelsScope, CopiedFieldScope copiedFieldScope,
                                                CompiledObject object) {
        if (widget.getQueryId() == null)
            return null;
        ClientDataProvider dataProvider = new ClientDataProvider();
        String url = getDatasourceRoute(widget, source, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + url);
        initDataProviderMappings(widget, dataProvider, p);
        SearchBarScope searchBarScope = p.getScope(SearchBarScope.class);
        if (searchBarScope != null && searchBarScope.getWidgetId().equals(source.getId())) {
            PageScope pageScope = p.getScope(PageScope.class);
            String searchWidgetId = pageScope != null ?
                    CompileUtil.generateWidgetId(pageScope.getPageId(), searchBarScope.getWidgetId()) :
                    searchBarScope.getWidgetId();
            ModelLink modelLink = new ModelLink(searchBarScope.getModelPrefix(),
                    pageScope == null || pageScope.getWidgetIdDatasourceMap() == null ?
                            searchWidgetId : pageScope.getWidgetIdDatasourceMap().get(searchWidgetId));
            modelLink.setFieldValue(searchBarScope.getModelKey());
            dataProvider.getQueryMapping().put(searchBarScope.getModelKey(), modelLink);

            if (!widget.containsFilter(searchBarScope.getModelKey())) {
                if (widget.getFilters() == null) widget.setFilters(new ArrayList<>());
                Filter filter = new Filter();
                filter.setFilterId(searchBarScope.getModelKey());
                filter.setLink(modelLink);
                filter.setRoutable(false);
                widget.getFilters().add(filter);
            }
        }

        p.addRoute(getQueryContext(widget, source, context, url, validationList, subModelsScope, copiedFieldScope, object));
        return dataProvider;
    }

    protected QueryContext getQueryContext(D widget, S source, CompileContext<?, ?> context, String route,
                                           ValidationList validationList, SubModelsScope subModelsScope,
                                           CopiedFieldScope copiedFieldScope, CompiledObject object) {
        QueryContext queryContext = new QueryContext(widget.getQueryId(), route, context.getUrlPattern());
        List<Validation> validations = validationList == null ? null : validationList.get(widget.getId(), ReduxModel.FILTER);
        if (context instanceof PageContext && ((PageContext) context).getSubmitOperationId() != null) {
            if (object == null)
                throw new N2oException("submit-operation is defined, but object-id isn't set in widget or query");
            CompiledObject.Operation operation = object.getOperations().get(((PageContext) context).getSubmitOperationId());
            if (operation.getValidationList() != null) {
                if (validations == null) {
                    validations = operation.getValidationList();
                } else {
                    validations.addAll(operation.getValidationList());
                }
            }
        }
        queryContext.setValidations(validations);
        queryContext.setFilters(widget.getFilters());
        queryContext.setUpload(widget.getUpload());
        queryContext.setFailAlertWidgetId(getFailAlertWidget(widget));
        queryContext.setSuccessAlertWidgetId(getSuccessAlertWidget(widget));
        queryContext.setMessagesForm(getMessagesForm(widget));
        if (source instanceof N2oForm) {
            queryContext.setSubModelQueries(subModelsScope.get(source.getDatasourceId()));
            queryContext.setQuerySize(1);
        } else {
            queryContext.setQuerySize(10);
        }
        if (copiedFieldScope != null) {
            queryContext.setCopiedFields(copiedFieldScope.getCopiedFields(source.getDatasourceId()));
        }
        return queryContext;
    }

    protected String getFailAlertWidget(Widget widget) {
        return widget.getId();
    }

    protected String getSuccessAlertWidget(Widget widget) {
        return widget.getId();
    }

    protected String getMessagesForm(Widget widget) {
        return widget.getId();
    }

    private void compileActions(S source, CompileContext<?, ?> context, CompileProcessor p
            , MetaActions widgetActions, WidgetScope widgetScope, ParentRouteScope widgetRouteScope, CompiledObject object, ValidationList validationList) {
        if (source.getActions() != null)
            for (ActionsBar a : source.getActions()) {
                a.setModel(p.cast(a.getModel(), ReduxModel.RESOLVE));
                p.compile(a.getAction(), context, widgetScope, widgetActions, widgetRouteScope, object, validationList, new ComponentScope(a));
            }
    }

    private void compileDependencies(D compiled, S source, CompileProcessor p) {
        WidgetDependency dependency = new WidgetDependency();
        String masterWidgetId = null;
        if (source.getDependsOn() != null) {
            List<DependencyCondition> fetch = new ArrayList<>();
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (widgetScope != null && widgetScope.getDependsOnWidgetId() != null) {
                masterWidgetId = widgetScope.getDependsOnWidgetId();//ds on
                PageScope pageScope = p.getScope(PageScope.class);
                String datasource = pageScope == null ? masterWidgetId :
                        pageScope.getWidgetIdDatasourceMap().get(masterWidgetId);
                ModelLink bindLink = new ModelLink(ReduxModel.RESOLVE, datasource);//pageId + datasource
                DependencyCondition condition = new DependencyCondition();
                condition.setGlobalMasterWidgetId(masterWidgetId);
                condition.setOn(bindLink.getBindLink());
                fetch.add(condition);
            }
            dependency.setFetch(fetch);
        }
        if (source.getVisible() != null) {
            Object condition = p.resolveJS(source.getVisible(), Boolean.class);
            if (StringUtils.isJs(condition)) {
                DependencyCondition visibilityCondition = new DependencyCondition();
                List<DependencyCondition> visible = new ArrayList<>();
                if (masterWidgetId != null) {
                    PageScope pageScope = p.getScope(PageScope.class);
                    String datasource = pageScope == null ? masterWidgetId
                            : pageScope.getWidgetIdDatasourceMap().get(pageScope.getGlobalWidgetId(source.getDependsOn()));
                    visibilityCondition.setOn(new ModelLink(ReduxModel.RESOLVE, datasource).getBindLink());
                }
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

    /**
     * Инициализация выборки виджета по id
     */
    private CompiledQuery getDataProviderQuery(String queryId, CompileProcessor p) {
        return queryId == null ? null : p.getCompiled(new QueryContext(queryId));
    }

    /**
     * Получить скомпилированную выборку
     */
    protected CompiledQuery getQuery(S source, CompileProcessor p) {
        CompiledQuery query = source.getQueryId() != null ? p.getCompiled(new QueryContext(source.getQueryId())) : null;
        if (query == null) {
            query = source.getDefaultValuesQueryId() != null ? p.getCompiled(new QueryContext(source.getDefaultValuesQueryId())) : null;
        }
        return query;
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
                                           CompiledObject widgetObject,
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
                    scopes, widgetQuery, widgetObject, widgetScope, fieldSetScope, indexScope));
        }
        return fieldSets;
    }

    private void initFilters(D compiled, S source, CompileProcessor p) {
        CompiledQuery query = getDataProviderQuery(compiled.getQueryId(), p);
        if (query == null)
            return;
        List<Filter> filters = new ArrayList<>();
        String masterWidgetId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        PageScope pageScope = p.getScope(PageScope.class);
        if (widgetScope != null) {
            masterWidgetId = widgetScope.getDependsOnWidgetId();
        }
        if (masterWidgetId != null && source.getDetailFieldId() != null) {
            Filter filter = new Filter();
            filter.setFilterId(query.getFilterFieldId(source.getDetailFieldId(), FilterType.eq));
            if (source.getMasterFieldId() == null || source.getMasterFieldId().equals(N2oQuery.Field.PK)) {
                if (compiled.getMasterParam() != null)
                    filter.setParam(compiled.getMasterParam());
                else
                    filter.setParam(normalizeParam(masterWidgetId + "_id"));
            } else {
                filter.setParam(normalizeParam(source.getMasterFieldId()));
            }
            filter.setRoutable(false);
            String masterFieldId = p.cast(source.getMasterFieldId(), N2oQuery.Field.PK);
            ModelLink link = Redux.linkQuery(masterWidgetId, masterFieldId, source.getQueryId());
            link.setParam(filter.getParam());
            filter.setLink(link);
            filters.add(filter);
        }
        if (source.getPreFilters() != null) {
            for (N2oPreFilter preFilter : source.getPreFilters()) {
                N2oQuery.Filter queryFilter = query.getFilterByPreFilter(preFilter);
                if (queryFilter != null) {
                    Filter filter = new Filter();
                    if (preFilter.getRequired() != null && preFilter.getRequired()) {
                        if (p.getScope(ValidationList.class) != null) {
                            MandatoryValidation v = new MandatoryValidation(
                                    queryFilter.getFilterField(),
                                    p.getMessage("n2o.required.filter"),
                                    queryFilter.getFilterField()
                            );
                            v.setMoment(N2oValidation.ServerMoment.beforeQuery);
                            v.setSeverity(SeverityType.danger);

                            if (p.getScope(ValidationList.class).get(compiled.getId(), ReduxModel.FILTER) == null) {
                                Map<String, List<Validation>> map = new HashMap<>();
                                map.put(compiled.getId(), new ArrayList<>());
                                p.getScope(ValidationList.class).getValidations().put(ReduxModel.FILTER, map);
                            }
                            List<Validation> validationList = p.getScope(ValidationList.class)
                                    .get(compiled.getId(), ReduxModel.FILTER);
                            validationList.add(v);
                        }
                    }
                    filter.setParam(p.cast(preFilter.getParam(), compiled.getId() + "_" + queryFilter.getParam()));
                    filter.setRoutable(p.cast(preFilter.getRoutable(), false));
                    filter.setFilterId(queryFilter.getFilterField());
                    Object prefilterValue = getPrefilterValue(preFilter);
                    ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
                    if (routeScope != null && routeScope.getQueryMapping() != null && routeScope.getQueryMapping().containsKey(filter.getParam())) {
                        //фильтр из родительского маршрута
                        filter.setLink(routeScope.getQueryMapping().get(filter.getParam()));
                    } else if (StringUtils.isJs(prefilterValue)) {
                        String refWidgetId = masterWidgetId;
                        if (preFilter.getRefWidgetId() != null) {
                            refWidgetId = preFilter.getRefPageId() == null ?
                                    pageScope.getGlobalWidgetId(preFilter.getRefWidgetId())
                                    : CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId());
                        }
                        String datasource = pageScope == null || pageScope.getWidgetIdDatasourceMap() == null
                                ? refWidgetId : pageScope.getWidgetIdDatasourceMap().get(refWidgetId);

                        ReduxModel model = p.cast(preFilter.getModel(), ReduxModel.RESOLVE);
                        ModelLink link = new ModelLink(model, datasource);
                        link.setValue(prefilterValue);
                        link.setParam(filter.getParam());
                        filter.setLink(link);
                    } else {
                        //фильтр с константным значением или значением из параметра в url
                        ModelLink link = new ModelLink(prefilterValue);
                        link.setParam(filter.getParam());
                        filter.setLink(link);
                    }
                    filters.add(filter);
                } else {
                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
                }
            }
        }
        compiled.setFilters(filters);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
