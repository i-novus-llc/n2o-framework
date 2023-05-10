package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.*;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.DynamicUtil.hasRefs;
import static net.n2oapp.framework.api.DynamicUtil.isDynamic;
import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Абстрактная реализация компиляция open-page, show-modal
 */
@Component
public abstract class AbstractOpenPageCompiler<D extends Action, S extends N2oAbstractPageAction> extends AbstractActionCompiler<D, S> {

    @Override
    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        if (source.getDatasources() != null) {
            for (N2oAbstractDatasource datasource : source.getDatasources()) {
                if (datasource instanceof N2oStandardDatasource)
                    initDefaultsDatasource((N2oStandardDatasource) datasource, p);
            }
        }
        if (source.getParams() != null) {
            for (N2oParam param : source.getParams()) {
                initDefaultsParam(param, p, source);
            }
        }
    }

    private void initDefaultsParam(N2oParam param, CompileProcessor p, S source) {
        param.setModel(p.cast(param.getModel(), () -> getModelFromComponentScope(p)));
        param.setDatasourceId(p.cast(param.getDatasourceId(), () -> getLocalDatasourceId(p)));
        if (param.getDatasourceId() == null && param.getValue() == null)
            throw new N2oException(String.format("datasource is not undefined for param %s of action %s", param.getName(), source.getId()));
        param.setRefPageId(p.cast(param.getRefPageId(), () -> {
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null)
                return pageScope.getPageId();
            else
                return null;
        }));
        if (param.getName() != null && param.getName().contains("$widgetId")) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            String clientWidgetId = widgetScope != null ? widgetScope.getClientWidgetId() : "";
            param.setName(param.getName().replace("$widgetId", clientWidgetId));//todo убрать после удаления adaptV1
        }
    }

    /**
     * Приведение поле источника данных к значениям по умолчанию
     *
     * @param datasource Источник данных
     * @param p          Процессор сборки
     */
    protected void initDefaultsDatasource(N2oStandardDatasource datasource, CompileProcessor p) {
        if (datasource.getFilters() != null) {
            for (N2oPreFilter filter : datasource.getFilters()) {
                filter.setModel(p.cast(filter.getModel(), () -> getModelFromComponentScope(p)));
                filter.setDatasourceId(p.cast(filter.getRefWidgetId(), () -> getLocalDatasourceId(p)));
                filter.setRefPageId(p.cast(filter.getRefPageId(), () -> {
                    PageScope pageScope = p.getScope(PageScope.class);
                    if (pageScope != null)
                        return pageScope.getPageId();
                    else
                        return null;
                }));
                if (filter.getParam() != null && filter.getParam().contains("$widgetId")) {
                    WidgetScope widgetScope = p.getScope(WidgetScope.class);
                    String clientWidgetId = widgetScope != null ? widgetScope.getClientWidgetId() : "";
                    filter.setParam(filter.getParam().replace("$widgetId", clientWidgetId));//todo убрать после удаления adaptV1
                }
            }
        }
    }

    protected abstract PageContext constructContext(String pageId, String route);

    protected PageContext initPageContext(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        validatePathAndRoute(source.getRoute(), source.getPathParams(), routeScope);
        String pageId = source.getPageId();
        ReduxModel actionDataModel = getModelFromComponentScope(p);
        PageScope pageScope = p.getScope(PageScope.class);
        String route = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");

        Map<String, ModelLink> pathMapping = new StrictMap<>();
        Map<String, ModelLink> queryMapping = new StrictMap<>();
        if (routeScope != null) {
            pathMapping.putAll(routeScope.getPathMapping());
            queryMapping.putAll(routeScope.getQueryMapping());
        }

        String currentClientWidgetId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            currentClientWidgetId = widgetScope.getClientWidgetId();

        ComponentScope componentScope = p.getScope(ComponentScope.class);
        ModelLink actionModelLink = createActionModelLink(actionDataModel, currentClientWidgetId, pageScope,
                componentScope, source.getPageId(), p);

        initPathMapping(source.getPathParams(), pathMapping, p);

        String actionRoute = initActionRoute(source, actionModelLink, pathMapping);
        String parentRoute = normalize(route);
        route = normalize(route + actionRoute) + (actionRoute.endsWith("/") ? "/" : "");
        PageContext pageContext = constructContext(pageId, route);
        if (pageScope != null && pageScope.getWidgetIdClientDatasourceMap() != null)
            pageContext.setParentWidgetIdDatasourceMap(pageScope.getWidgetIdClientDatasourceMap());
        if (pageScope != null && pageScope.getTabIds() != null)
            pageContext.setParentTabIds(pageScope.getTabIds());

        pageContext.setPageName(source.getPageName());
        pageContext.setBreadcrumbs(initBreadcrumb(source, pageContext, p));
        // copy
        pageContext.setCopyModel(source.getCopyModel());
        pageContext.setCopyDatasourceId(source.getCopyDatasourceId());
        pageContext.setCopyFieldId(source.getCopyFieldId());
        pageContext.setTargetModel(source.getTargetModel());
        pageContext.setTargetDatasourceId(computeTargetDatasource(source, pageScope, componentScope, widgetScope));
        pageContext.setTargetFieldId(source.getTargetFieldId());
        pageContext.setTargetPage(source.getTargetPage());
        pageContext.setCopyMode(source.getCopyMode());

        if (source.getDatasources() != null) {
            if (pageContext.getDatasources() == null)
                pageContext.setDatasources(new ArrayList<>());
            pageContext.getDatasources().addAll(Arrays.asList(source.getDatasources()));
        }
        if (source.getToolbars() != null)
            pageContext.setToolbars(Arrays.asList(source.getToolbars()));
        if (source.getActions() != null)
            pageContext.setActions(Arrays.stream(source.getActions())
                    .collect(Collectors.toMap(ActionBar::getId, Function.identity())));

        String parentWidgetId = initWidgetId(p);
        pageContext.setParentWidgetId(parentWidgetId);
        pageContext.setParentClientWidgetId(currentClientWidgetId);
        String localDatasourceId = getLocalDatasourceId(p);
        pageContext.setParentLocalDatasourceId(localDatasourceId);
        pageContext.setParentClientPageId(pageScope == null ? null : pageScope.getPageId());
        pageContext.setParentRoute(RouteUtil.addQueryParams(parentRoute, queryMapping));
        if (context instanceof PageContext) {
            pageContext.addParentRoute(pageContext.getParentRoute(), ((PageContext) context));
            pageContext.setParentDatasourceIdsMap(initParentDatasourceIdsMap(p, (PageContext) context));
        }
        pageContext.setRefreshOnClose(p.cast(source.getRefreshOnClose(), false));
        if ((source.getRefreshAfterSubmit() == null || source.getRefreshAfterSubmit() || pageContext.getRefreshOnClose()) &&
                (source.getRefreshDatasourceIds() != null || localDatasourceId != null)) {
            String[] refreshDatasourceIds = source.getRefreshDatasourceIds() == null ?
                    new String[]{localDatasourceId} : source.getRefreshDatasourceIds();
            if (pageScope != null) {
                pageContext.setRefreshClientDataSourceIds(Arrays.stream(refreshDatasourceIds)
                        .map(d -> getClientDatasourceId(d, p)).collect(Collectors.toList()));
            }
        }
        pageContext.setUnsavedDataPromptOnClose(p.cast(source.getUnsavedDataPromptOnClose(), true));
        pageContext.setPathRouteMapping(pathMapping);
        initQueryMapping(source.getQueryParams(), pathMapping, queryMapping, p);
        pageContext.setQueryRouteMapping(queryMapping);
        pageContext.setParentModelLinks(collectParentLinks(actionModelLink, pathMapping.values(), queryMapping.values()));

        initPageRoute(compiled, route, pathMapping, queryMapping);
        initOtherPageRoute(p, context, route);
        p.addRoute(pageContext);

        if (source.getToolbars() != null)
            pageContext.setToolbars(Arrays.asList(source.getToolbars()));

        return pageContext;
    }

    private Map<String, String> initParentDatasourceIdsMap(CompileProcessor p, PageContext context) {
        Map<String, String> parentDatasourceIdsMap = new HashMap<>();

        DataSourcesScope scope = p.getScope(DataSourcesScope.class);
        if (!CollectionUtils.isEmpty(scope))
            for (Map.Entry<String, N2oAbstractDatasource> entry : scope.entrySet())
                if (entry.getValue() instanceof N2oParentDatasource) {
                    if (!((N2oParentDatasource) entry.getValue()).isFromParentPage())
                        parentDatasourceIdsMap.put(entry.getKey(), context.getParentDatasourceIdsMap().get(entry.getKey()));
                } else
                    parentDatasourceIdsMap.put(entry.getKey(), getClientDatasourceId(entry.getKey(), p));

        return parentDatasourceIdsMap;
    }

    private String computeTargetDatasource(S source, PageScope pageScope, ComponentScope componentScope, WidgetScope widgetScope) {
        String currentWidgetId = null;
        if (widgetScope != null) {
            currentWidgetId = widgetScope.getWidgetId();
        }

        String targetDatasourceId = source.getTargetDatasourceId();
        if (pageScope != null && targetDatasourceId == null) {
            DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
            if (nonNull(datasourceIdAware) && nonNull(datasourceIdAware.getDatasourceId()))
                targetDatasourceId = datasourceIdAware.getDatasourceId();
            else if (currentWidgetId != null)
                targetDatasourceId = pageScope.getWidgetIdSourceDatasourceMap().get(currentWidgetId);
        }
        return targetDatasourceId;
    }

    private List<Breadcrumb> initBreadcrumb(S source, PageContext pageContext, CompileProcessor p) {
        if (source.getBreadcrumbs() != null) {
            pageContext.setBreadcrumbFromParent(true);
            return Arrays.stream(source.getBreadcrumbs())
                    .map(crumb -> new Breadcrumb(crumb.getLabel(), crumb.getPath()))
                    .collect(Collectors.toList());
        }

        pageContext.setBreadcrumbFromParent(false);
        return p.getScope(BreadcrumbList.class);
    }

    /**
     * Сбор родительских ссылок на модели в список в порядке приоритета их использования для разрешения
     * параметров открываемой страницы
     *
     * @param actionModelLink Ссылка на модель действия
     * @param pathLinks       Ссылки на модели параметров пути
     * @param queryLinks      Ссылки на модели параметров запроса
     * @return список родительских ссылок
     */
    //TODO убрать в рамках рефакторинга https://jira.i-novus.ru/browse/NNO-8532
    protected List<ModelLink> collectParentLinks(ModelLink actionModelLink, Collection<ModelLink> pathLinks, Collection<ModelLink> queryLinks) {
        List<ModelLink> links = new ArrayList<>();
        links.add(actionModelLink);
        links.addAll(pathLinks);//TODO возможно стоит добавить сортировку по использованию в route
        links.addAll(queryLinks);
        return links;
    }

    /**
     * Создание ссылки на модель действия
     *
     * @param actionDataModel Модель действия
     * @param clientWidgetId  Идентификатор клиентского виджета
     * @param pageScope       Информация о странице
     * @param componentScope  Информация о родительском компоненте
     * @param pageId          Идентификатор открываемой страницы
     * @return Ссылка на модель действия
     */
    private ModelLink createActionModelLink(ReduxModel actionDataModel, String clientWidgetId, PageScope pageScope,
                                            ComponentScope componentScope, String pageId, CompileProcessor p) {
        if (componentScope != null) {
            String datasource;
            DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
            if (datasourceIdAware != null && datasourceIdAware.getDatasourceId() != null) {
                datasource = getClientDatasourceId(datasourceIdAware.getDatasourceId(), p);
            } else {
                datasource = (pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null)
                        ? clientWidgetId
                        : pageScope.getWidgetIdClientDatasourceMap().get(clientWidgetId);
            }
            return new ModelLink(actionDataModel, datasource, isLink(pageId) ? unwrapLink(pageId) : QuerySimpleField.PK);
        }
        return null;
    }

    /**
     * Добавление параметров пути в pathMapping
     *
     * @param params      Список входящих параметров пути
     * @param pathMapping Map моделей параметров пути
     *                    В нее будут добавлены модели построенных параметров пути
     * @param p           Процессор сборки метаданных
     */
    private void initPathMapping(N2oParam[] params, Map<String, ModelLink> pathMapping, CompileProcessor p) {
        if (params == null || params.length == 0) return;

        List<N2oParam> resultParams = prepareParams(params);
        pathMapping.putAll(initParams(resultParams, pathMapping, p));
    }

    /**
     * Добавление параметров запроса в queryMapping
     *
     * @param params       Список входящих параметров запроса
     * @param pathMapping  Map моделей параметров пути
     * @param queryMapping Map моделей параметров запроса.
     *                     В нее будут добавлены модели построенных параметров запроса
     * @param p            Процессор сборки метаданных
     */
    private void initQueryMapping(N2oParam[] params, Map<String, ModelLink> pathMapping,
                                  Map<String, ModelLink> queryMapping, CompileProcessor p) {
        if (params == null || params.length == 0) return;
        List<N2oParam> resultParams = prepareParams(params);
        queryMapping.putAll(initParams(resultParams, pathMapping, p));
    }

    /**
     * Подготовка параметров
     *
     * @param params Список входящих параметров
     * @return Список преобразованных параметров
     */
    private List<N2oParam> prepareParams(N2oParam[] params) {
        List<N2oParam> resultParams = new ArrayList<>();
        for (N2oParam param : params) {
            resultParams.add(new N2oParam(param));
        }
        return resultParams;
    }

    /**
     * Инициализация map моделей по имени параметра из списка параметров.
     *
     * @param params     Список параметров
     * @param pathParams Map моделей по имени параметра
     *                   Используется для фильтрации параметров, не входящих в данную map
     * @return Map моделей по имени параметра
     */
    private Map<String, ModelLink> initParams(List<N2oParam> params,
                                              Map<String, ModelLink> pathParams,
                                              CompileProcessor p) {
        return params == null ? null :
                params.stream().filter(f -> f.getName() != null && !pathParams.containsKey(f.getName()))
                        .collect(Collectors.toMap(N2oParam::getName, param -> {
                            ModelLink link = Redux.linkParam(param, p);
                            String datasource = param.getDatasourceId();
                            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
                            if (ReduxModel.resolve.equals(link.getModel()) && Objects.equals(link.getFieldId(), "id")
                                    && dataSourcesScope.get(datasource) != null && dataSourcesScope.get(datasource) instanceof N2oStandardDatasource)
                                link.setSubModelQuery(new SubModelQuery(((N2oStandardDatasource) dataSourcesScope.get(datasource)).getQueryId()));
                            return link;
                        }));
    }

    /**
     * Построение маршрута действия
     *
     * @param source          Действие
     * @param actionModelLink Ссылка на модель действия
     * @param pathMapping     Map моделей параметров пути
     * @return Маршрут действия
     */
    private String initActionRoute(S source, ModelLink actionModelLink, Map<String, ModelLink> pathMapping) {
        String actionRoute = source.getRoute();
        if (actionRoute == null) {
            actionRoute = normalize(source.getId());
            // генерация маршрута для динамической страницы с моделью resolve
            boolean isDynamicPage = hasRefs(source.getPageId()) || isDynamic(source.getPageId());
            if (isDynamicPage && actionModelLink != null && ReduxModel.resolve.equals(actionModelLink.getModel())) {
                String masterIdParam = actionModelLink.getDatasource() + "_id";
                String dynamicPageActionRoute = normalize(colon(masterIdParam)) + actionRoute;
                pathMapping.put(masterIdParam, actionModelLink);
                return dynamicPageActionRoute;
            }
        }
        return actionRoute;
    }

    protected abstract void initPageRoute(D compiled, String route,
                                          Map<String, ModelLink> pathMapping,
                                          Map<String, ModelLink> queryMapping);

    private void initOtherPageRoute(CompileProcessor p, CompileContext<?, ?> context, String route) {
        if (context instanceof ModalPageContext)
            return;
        //only for link
        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null) {
            PageRoutes.Route pageRoute = new PageRoutes.Route(route);
            pageRoute.setIsOtherPage(true);
            pageRoutes.addRoute(pageRoute);
        }
    }

    /**
     * Получение виджета действия (исходный)
     */
    private String initWidgetId(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                return widgetIdAware.getWidgetId();
            }
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getWidgetId();
        return null;
    }

    protected void validatePathAndRoute(String route, N2oParam[] pathParams, ParentRouteScope routeScope) {
        List<String> routeParams = route == null ? null : RouteUtil.getParams(route);
        if ((routeParams == null || routeParams.isEmpty()) && (pathParams == null || pathParams.length == 0)) return;

        if (routeParams == null)
            throw new N2oException(String.format("path-param \"%s\" not used in route", pathParams[0].getName()));
        if (pathParams == null)
            throw new N2oException(String.format("path-param \"%s\" for route \"%s\" not set", routeParams.get(0), route));

        for (N2oParam pathParam : pathParams) {
            if (!routeParams.contains(pathParam.getName()))
                throw new N2oException(String.format("route \"%s\" not contains path-param \"%s\"", route, pathParam.getName()));
            if (routeScope != null && routeScope.getUrl() != null && RouteUtil.getParams(routeScope.getUrl()).contains(pathParam.getName()))
                throw new N2oException(String.format("param \"%s\" duplicate in parent url ", pathParam.getName()));
        }
    }

    /**
     * Поддержка старых атрибутов submit-operation-id и тд
     */
    @Deprecated
    protected void initToolbarBySubmitOperation(S source, PageContext context, CompileProcessor p) {
        if (source.getSubmitOperationId() != null || SubmitActionType.copy.equals(source.getSubmitActionType())) {
            N2oToolbar n2oToolbar = new N2oToolbar();
            if (context.getToolbars() == null) {
                context.setToolbars(new ArrayList<>());
            }
            ToolbarItem[] items = new ToolbarItem[2];
            n2oToolbar.setItems(items);
            context.getToolbars().add(n2oToolbar);

            //create submit button
            N2oButton saveButton = new N2oButton();
            saveButton.setId(GenerateType.submit.name());
            saveButton.setColor("primary");
            N2oAction[] actions = null;
            ReduxModel saveButtonModel = null;
            SubmitActionType submitActionType = source.getSubmitActionType() == null ? SubmitActionType.invoke : source.getSubmitActionType();
            Boolean closeOnSuccess = p.cast(source.getCloseAfterSubmit(), true);
            Boolean refreshOnSuccessSubmit = p.cast(source.getRefreshAfterSubmit(), true);

            switch (submitActionType) {
                case copy: {
                    N2oCopyAction copyAction = new N2oCopyAction();
                    copyAction.setSourceModel(source.getCopyModel());
                    copyAction.setSourceDatasourceId(source.getCopyDatasourceId());
                    copyAction.setSourceFieldId(source.getCopyFieldId());
                    copyAction.setTargetModel(source.getTargetModel());
                    copyAction.setTargetPage(p.cast(source.getTargetPage(), PageRef.PARENT));
                    if (copyAction.getTargetPage().equals(PageRef.PARENT)) {
                        copyAction.setTargetDatasourceId(p.cast(source.getTargetDatasourceId(), getLocalDatasourceId(p)));
                    } else {
                        copyAction.setTargetDatasourceId(source.getTargetDatasourceId());
                    }
                    copyAction.setTargetFieldId(source.getTargetFieldId());
                    copyAction.setMode(source.getCopyMode());
                    copyAction.setCloseOnSuccess(closeOnSuccess);
                    actions = new N2oAction[]{copyAction};
                    saveButtonModel = source.getCopyModel();
                }
                break;
                case invoke: {
                    List<N2oAction> actionList = new ArrayList<>();
                    N2oInvokeAction invokeAction = new N2oInvokeAction();
                    actionList.add(invokeAction);
                    if (refreshOnSuccessSubmit) {
                        if (closeOnSuccess) {
                            invokeAction.setCloseOnSuccess(false);
                            invokeAction.setRefreshOnSuccess(false);
                            String[] refreshDatasourceIds = getRefreshDatasourceId(source, p);
                            for (String refreshDatasourceId : refreshDatasourceIds) {
                                // добавляем refresh action для каждого датасурса
                                N2oRefreshAction refreshAction = new N2oRefreshAction();
                                refreshAction.setDatasourceId(refreshDatasourceId);
                                refreshAction.setPage(PageRef.PARENT);
                                actionList.add(refreshAction);
                                // добавляем parent-datasource чтобы в модалке был этот датасурс
                                if (context.getDatasources() == null)
                                    context.setDatasources(new ArrayList<>());
                                context.getDatasources().add(new N2oParentDatasource("parent_" + refreshDatasourceId, refreshDatasourceId, false));
                            }
                            N2oCloseAction closeAction = new N2oCloseAction();
                            closeAction.setPrompt(false);
                            actionList.add(closeAction);
                        } else {
                            invokeAction.setRefreshOnSuccess(true);
                            invokeAction.setCloseOnSuccess(false);
                            invokeAction.setRefreshDatasourceIds(source.getRefreshDatasourceIds());
                        }
                    } else {
                        invokeAction.setCloseOnSuccess(closeOnSuccess);
                        invokeAction.setRefreshOnSuccess(false);
                    }

                    if (source.getRedirectUrlAfterSubmit() != null) {
                        invokeAction.setRedirectTarget(p.cast(source.getRedirectTargetAfterSubmit(),
                                RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
                        invokeAction.setRedirectUrl(source.getRedirectUrlAfterSubmit());
                    }

                    invokeAction.setOperationId(source.getSubmitOperationId());
                    actions = actionList.toArray(new N2oAction[0]);
                    saveButtonModel = source.getSubmitModel();
                }
                break;
            }
            saveButton.setLabel(p.cast(source.getSubmitLabel(), p.getMessage("n2o.api.action.toolbar.button.submit.label")));
            saveButton.setActions(actions);
            saveButton.setModel(p.cast(saveButtonModel, ReduxModel.resolve));
            saveButton.setValidate(true);
            items[0] = saveButton;

            //create close button
            N2oButton closeButton = new N2oButton();
            closeButton.setId(GenerateType.close.name());
            closeButton.setLabel(p.getMessage("n2o.api.action.toolbar.button.close.label"));
            N2oCloseAction cancelAction = new N2oCloseAction();
            cancelAction.setId(GenerateType.close.name());
            closeButton.setModel(ReduxModel.filter);
            cancelAction.setRefresh(source.getRefreshOnClose());
            closeButton.setActions(new N2oCloseAction[]{cancelAction});
            closeButton.setValidate(false);
            items[1] = closeButton;
        }
    }

    protected String[] getRefreshDatasourceId(S source, CompileProcessor p) {
        if (source.getRefreshDatasourceIds() != null) return source.getRefreshDatasourceIds();
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null) {
            String parentWidgetId = initWidgetId(p);
            String datasourceId = pageScope.getWidgetIdSourceDatasourceMap().get(parentWidgetId);
            if (datasourceId != null)
                return new String[]{datasourceId};
        }
        return null;
    }

}
