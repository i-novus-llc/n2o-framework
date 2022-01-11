package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.DynamicUtil.hasRefs;
import static net.n2oapp.framework.api.DynamicUtil.isDynamic;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.global.dao.N2oQuery.Field.PK;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Абстрактная реализация компиляция open-page, show-modal
 */
@Component
public abstract class AbstractOpenPageCompiler<D extends Action, S extends N2oAbstractPageAction> extends AbstractActionCompiler<D, S> {

    @Override
    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        if (source.getDatasources() != null) {
            for (N2oDatasource datasource : source.getDatasources()) {
                initDefaultsDatasource(datasource, context, p);
            }
        }
        if (source.getParams() != null) {
            for (N2oParam param : source.getParams()) {
                initDefaultsParam(param, p);
            }
        }
    }

    private void initDefaultsParam(N2oParam param, CompileProcessor p) {
        param.setModel(p.cast(param.getModel(), () -> getModelFromComponentScope(p)));
        param.setRefWidgetId(p.cast(param.getRefWidgetId(), () -> initWidgetId(p)));
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
     * @param context    Контекст сборки
     * @param p          Процессор сборки
     */
    protected void initDefaultsDatasource(N2oDatasource datasource, CompileContext<?, ?> context, CompileProcessor p) {
        if (datasource.getFilters() != null) {
            for (N2oPreFilter filter : datasource.getFilters()) {
                filter.setModel(p.cast(filter.getModel(), () -> getModelFromComponentScope(p)));
                filter.setRefWidgetId(p.cast(filter.getRefWidgetId(), () -> initWidgetId(p)));
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

    protected List<N2oPreFilter> initPreFilters(N2oAbstractPageAction source, String actionRoute, String parentWidgetId, CompileProcessor p) {
        List<N2oPreFilter> preFilters = new ArrayList<>();
        ReduxModel model = getModelFromComponentScope(p);
        PageScope pageScope = p.getScope(PageScope.class);

        if (source.getDetailFieldId() != null) {
            N2oPreFilter filter = new N2oPreFilter();
            filter.setFieldId(p.cast(source.getDetailFieldId(), PK));
            filter.setType(FilterType.eq);
            filter.setValueAttr(Placeholders.ref(p.cast(source.getMasterFieldId(), PK)));
            filter.setRefWidgetId(parentWidgetId);
            if ((source.getMasterFieldId() == null || source.getMasterFieldId().equals(PK)) && ReduxModel.resolve.equals(model)) {
                List<String> actionRouteParams = RouteUtil.getParams(actionRoute);
                String masterIdParam = actionRouteParams.isEmpty() ? null : actionRouteParams.get(0);
                filter.setParam(p.cast(source.getMasterParam(), masterIdParam, createGlobalParam(filter.getFieldId(), p)));
            } else {
                filter.setParam(p.cast(source.getMasterParam(), createGlobalParam(filter.getFieldId(), p)));
            }
            filter.setModel(ReduxModel.resolve);
            if (pageScope != null) {
                filter.setRefPageId(pageScope.getPageId());
            }
            preFilters.add(filter);
        }
        if (source.getPreFilters() != null) {
            for (N2oPreFilter preFilter : source.getPreFilters()) {
                N2oPreFilter filter = new N2oPreFilter();
                filter.setFieldId(preFilter.getFieldId());
                filter.setParam(p.cast(preFilter.getParam(), createGlobalParam(filter.getFieldId(), p)));
                filter.setType(preFilter.getType());
                filter.setValueAttr(preFilter.getValueAttr());
                filter.setValuesAttr(preFilter.getValuesAttr());
                filter.setRefWidgetId(p.cast(preFilter.getRefWidgetId(), parentWidgetId));
                filter.setModel(p.cast(preFilter.getModel(), model));
                if (pageScope != null) {
                    filter.setRefPageId(pageScope.getPageId());
                }
                preFilters.add(filter);
            }
        }
        return preFilters;
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
        String currentWidgetId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            currentClientWidgetId = widgetScope.getClientWidgetId();
            currentWidgetId = widgetScope.getWidgetId();
        }

        ComponentScope componentScope = p.getScope(ComponentScope.class);
        ModelLink actionModelLink = createActionModelLink(actionDataModel, currentClientWidgetId, pageScope, componentScope);

        String actionRoute = initActionRoute(source, actionModelLink, pathMapping);

        String parentComponentWidgetId = null;
        WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
        if (widgetIdAware != null && widgetIdAware.getWidgetId() != null)
            parentComponentWidgetId = widgetIdAware.getWidgetId();

        // виджет для текущей модели берется либо из родителя, либо текущий
        String actionModelWidgetId = p.cast(parentComponentWidgetId, currentWidgetId);

        Map<String, String> widgetIdQueryIdMap = null;
        if (pageScope != null && !CollectionUtils.isEmpty(pageScope.getWidgetIdQueryIdMap()))
            widgetIdQueryIdMap = pageScope.getWidgetIdQueryIdMap();
        initPathMapping(source.getPathParams(), actionDataModel, pathMapping, pageScope, actionModelWidgetId, widgetIdQueryIdMap, p);

        String parentRoute = normalize(route);
        route = normalize(route + actionRoute);
        PageContext pageContext = constructContext(pageId, route);
        if (pageScope != null && pageScope.getWidgetIdClientDatasourceMap() != null)
            pageContext.setParentWidgetIdDatasourceMap(pageScope.getWidgetIdClientDatasourceMap());
        if (pageScope != null && pageScope.getTabIds() != null)
            pageContext.setParentTabIds(pageScope.getTabIds());
        String targetDS = source.getTargetDatasource();
        if (pageScope != null && targetDS == null && currentWidgetId != null) {
            targetDS = pageScope.getWidgetIdSourceDatasourceMap().get(currentWidgetId);
        }
        pageContext.setPageName(source.getPageName());
        pageContext.setBreadcrumbs(p.getScope(BreadcrumbList.class));
        pageContext.setSubmitOperationId(source.getSubmitOperationId());
        pageContext.setSubmitLabel(source.getSubmitLabel());
        pageContext.setSubmitModel(source.getSubmitModel());
        pageContext.setSubmitActionType(source.getSubmitActionType());
        pageContext.setCopyModel(source.getCopyModel());
        pageContext.setCopyWidgetId(source.getCopyWidgetId());
        pageContext.setCopyDatasource(source.getCopyDatasource());
        pageContext.setCopyFieldId(source.getCopyFieldId());
        pageContext.setTargetModel(source.getTargetModel());
        pageContext.setTargetDatasourceId(targetDS);
        pageContext.setTargetFieldId(source.getTargetFieldId());
        pageContext.setCopyMode(source.getCopyMode());
        if (source.getDatasources() != null)
            pageContext.setDatasources(Arrays.asList(source.getDatasources()));
        String parentWidgetId = initWidgetId(p);
        pageContext.setParentWidgetId(parentWidgetId);
        pageContext.setParentClientWidgetId(currentClientWidgetId);
        pageContext.setParentClientPageId(pageScope == null ? null : pageScope.getPageId());
        pageContext.setParentModelLink(actionModelLink);
        pageContext.setParentRoute(RouteUtil.addQueryParams(parentRoute, queryMapping));
        pageContext.setCloseOnSuccessSubmit(p.cast(source.getCloseAfterSubmit(), true));
        pageContext.setRefreshOnSuccessSubmit(p.cast(source.getRefreshAfterSubmit(), true));
        if (source.getRefreshDatasources() != null) {
            pageContext.setRefreshClientDataSources(Arrays.stream(source.getRefreshDatasources())
                    .map(pageScope::getClientDatasourceId).collect(Collectors.toList()));
        }
        if (pageContext.getCloseOnSuccessSubmit() && pageContext.getRefreshClientDataSources() == null && pageScope != null) {
            String datasourceId = pageScope.getWidgetIdClientDatasourceMap().get(pageScope.getGlobalWidgetId(parentWidgetId));
            if (datasourceId != null)
                pageContext.setRefreshClientDataSources(Arrays.asList(datasourceId));
        }
        pageContext.setRefreshOnClose(p.cast(source.getRefreshOnClose(), false));
        pageContext.setUnsavedDataPromptOnClose(p.cast(source.getUnsavedDataPromptOnClose(), true));
        if (source.getRedirectUrlAfterSubmit() != null) {
            pageContext.setRedirectUrlOnSuccessSubmit(source.getRedirectUrlAfterSubmit());
            pageContext.setRedirectTargetOnSuccessSubmit(p.cast(source.getRedirectTargetAfterSubmit(),
                    RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
        }
        pageContext.setPathRouteMapping(pathMapping);
        // при наличии route или при filter модели не добавляем queryMapping
//        if (source.getRoute() == null && !ReduxModel.filter.equals(actionDataModel))
//            queryMapping.putAll(initPreFilterParams(preFilters, pathMapping, p)); fixme кажется не нужно, после появления datasources
        initQueryMapping(source.getQueryParams(), actionDataModel, pathMapping, queryMapping, pageScope,
                actionModelWidgetId, widgetIdQueryIdMap, p);
        pageContext.setQueryRouteMapping(queryMapping);

        initPageRoute(compiled, route, pathMapping, queryMapping);
        initOtherPageRoute(p, context, route);
        p.addRoute(pageContext);
        return pageContext;
    }

    /**
     * Создание ссылки на модель действия
     *
     * @param actionDataModel Модель действия
     * @param clientWidgetId  Идентификатор клиентского виджета
     * @param pageScope       Информация о странице
     * @param componentScope  Информация о родительском компоненте
     * @return Ссылка на модель действия
     */
    private ModelLink createActionModelLink(ReduxModel actionDataModel, String clientWidgetId,
                                            PageScope pageScope, ComponentScope componentScope) {
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            String actionDataModelClientWidgetId = null;
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null)
                actionDataModelClientWidgetId = pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());
            else
                actionDataModelClientWidgetId = clientWidgetId;

            if (actionDataModel != null && actionDataModelClientWidgetId != null) {
                String datasource = (pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null)
                        ? actionDataModelClientWidgetId
                        : pageScope.getWidgetIdClientDatasourceMap().get(actionDataModelClientWidgetId);
                return new ModelLink(actionDataModel, datasource, N2oQuery.Field.PK);
            }
        }
        return null;
    }

    /**
     * Добавление параметров пути в pathMapping
     *
     * @param params                  Список входящих параметров пути
     * @param actionDataModel         Модель действия
     * @param pathMapping             Map моделей параметров пути
     *                                В нее будут добавлены модели построенных параметров пути
     * @param pageScope               Информация о странице
     * @param defaultParamRefWidgetId Идентификатор виджета, который будет использоваться по умолчанию,
     *                                в случае, если в параметре не задан виджет
     * @param widgetIdQueryIdMap      Map идентификатора выборки по идентификатору виджета
     * @param p                       Процессор сборки метаданных
     */
    private void initPathMapping(N2oParam[] params, ReduxModel actionDataModel, Map<String, ModelLink> pathMapping,
                                 PageScope pageScope, String defaultParamRefWidgetId, Map<String, String> widgetIdQueryIdMap,
                                 CompileProcessor p) {
        if (params == null || params.length == 0) return;

        List<N2oParam> resultParams = prepareParams(params);
        pathMapping.putAll(initParams(resultParams, pathMapping, widgetIdQueryIdMap, p));
    }

    /**
     * Добавление параметров запроса в queryMapping
     *
     * @param params                  Список входящих параметров запроса
     * @param actionDataModel         Модель действия
     * @param pathMapping             Map моделей параметров пути
     * @param queryMapping            Map моделей параметров запроса.
     *                                В нее будут добавлены модели построенных параметров запроса
     * @param pageScope               Информация о странице
     * @param defaultParamRefWidgetId Идентификатор виджета, который будет использоваться по умолчанию,
     *                                в случае, если в параметре не задан виджет
     * @param widgetIdQueryIdMap      Map идентификатора выборки по идентификатору виджета
     * @param p                       Процессор сборки метаданных
     */
    private void initQueryMapping(N2oParam[] params, ReduxModel actionDataModel, Map<String, ModelLink> pathMapping,
                                  Map<String, ModelLink> queryMapping, PageScope pageScope,
                                  String defaultParamRefWidgetId, Map<String, String> widgetIdQueryIdMap, CompileProcessor p) {
        if (params == null || params.length == 0) return;
        List<N2oParam> resultParams = prepareParams(params);
        queryMapping.putAll(initParams(resultParams, pathMapping, widgetIdQueryIdMap, p));
    }

    /**
     * Подготовка параметров
     *
     * @param params                  Список входящих параметров
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
     * @param params             Список параметров
     * @param pathParams         Map моделей по имени параметра
     *                           Используется для фильтрации параметров, не входящих в данную map
     * @param widgetIdQueryIdMap Map идентификатора выборки по идентификатору виджета
     * @return Map моделей по имени параметра
     */
    private Map<String, ModelLink> initParams(List<N2oParam> params,
                                              Map<String, ModelLink> pathParams,
                                              Map<String, String> widgetIdQueryIdMap,
                                              CompileProcessor p) {
        return params == null ? null :
                params.stream().filter(f -> f.getName() != null && !pathParams.containsKey(f.getName()))
                        .collect(Collectors.toMap(N2oParam::getName, param -> {
                            String widgetId = param.getRefWidgetId();
                            ModelLink link = Redux.linkParam(param, p);
                            if (ReduxModel.resolve.equals(link.getModel()) && Objects.equals(link.getFieldId(), "id")
                                    && widgetIdQueryIdMap != null
                                    && widgetIdQueryIdMap.get(widgetId) != null)
                                link.setSubModelQuery(new SubModelQuery(widgetIdQueryIdMap.get(widgetId)));
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


    private Map<String, ModelLink> initPreFilterParams(List<N2oPreFilter> preFilters,
                                                       Map<String, ModelLink> pathParams,
                                                       CompileProcessor p) {
        return preFilters == null ? null :
                preFilters.stream().filter(f -> f.getParam() != null && !pathParams.containsKey(f.getParam()))
                        .collect(Collectors.toMap(N2oPreFilter::getParam, pf -> Redux.linkParam(pf, p)));
    }

    private String createGlobalParam(String param, CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope == null || widgetScope.getClientWidgetId() == null) {
            return param;
        }
        return widgetScope.getClientWidgetId() + "_" + param;
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
}