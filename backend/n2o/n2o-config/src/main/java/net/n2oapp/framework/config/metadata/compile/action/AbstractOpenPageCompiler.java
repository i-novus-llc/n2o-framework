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
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.Page;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    protected List<N2oPreFilter> initPreFilters(N2oAbstractPageAction source, String actionRoute, String parentWidgetId, CompileProcessor p) {
        List<N2oPreFilter> preFilters = new ArrayList<>();
        ReduxModel model = getTargetWidgetModel(p, ReduxModel.RESOLVE);
        String widgetId = parentWidgetId;
        PageScope pageScope = p.getScope(PageScope.class);
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                widgetId = widgetIdAware.getWidgetId();
            }
        }

        if (source.getDetailFieldId() != null) {
            N2oPreFilter filter = new N2oPreFilter();
            filter.setFieldId(p.cast(source.getDetailFieldId(), PK));
            filter.setType(FilterType.eq);
            filter.setValueAttr(Placeholders.ref(p.cast(source.getMasterFieldId(), PK)));
            filter.setRefWidgetId(widgetId);
            if ((source.getMasterFieldId() == null || source.getMasterFieldId().equals(PK)) && ReduxModel.RESOLVE.equals(model)) {
                List<String> actionRouteParams = RouteUtil.getParams(actionRoute);
                String masterIdParam = actionRouteParams.isEmpty() ? null : actionRouteParams.get(0);
                filter.setParam(p.cast(source.getMasterParam(), masterIdParam, createGlobalParam(filter.getFieldId(), p)));
            } else {
                filter.setParam(p.cast(source.getMasterParam(), createGlobalParam(filter.getFieldId(), p)));
            }
            filter.setRefModel(ReduxModel.RESOLVE);
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
                filter.setRefWidgetId(p.cast(preFilter.getRefWidgetId(), widgetId));
                filter.setRefModel(p.cast(preFilter.getRefModel(), model));
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
        ReduxModel actionDataModel = getTargetWidgetModel(p, ReduxModel.RESOLVE);
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
        if (actionModelLink == null)
            throw new N2oException("widget-id for action " + source.getId() + " not specified");

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
        if (widgetScope != null && Boolean.TRUE.equals(widgetScope.getHasIdInParentRoute())) {
            List<String> pathParams = RouteUtil.getPathParams(actionRoute);
            if (!pathParams.isEmpty())
                parentRoute = normalize(parentRoute + "/:" + pathParams.get(0));
        }
        route = normalize(route + actionRoute);

        PageContext pageContext = constructContext(pageId, route);
        if (pageScope != null) {
            if (pageScope.getWidgetIdDatasourceMap() != null)
                pageContext.setParentWidgetIdDatasourceMap(pageScope.getWidgetIdDatasourceMap());
            if (pageScope.getTabIds() != null)
                pageContext.setParentTabIds(pageScope.getTabIds());
        }
        pageContext.setPageName(source.getPageName());
        pageContext.setBreadcrumbs(p.getScope(BreadcrumbList.class));
        pageContext.setSubmitOperationId(source.getSubmitOperationId());
        pageContext.setSubmitLabel(source.getSubmitLabel());
        pageContext.setSubmitModel(source.getSubmitModel());
        pageContext.setSubmitActionType(source.getSubmitActionType());
        pageContext.setCopyModel(source.getCopyModel());
        pageContext.setCopyWidgetId(source.getCopyWidgetId());
        pageContext.setCopyFieldId(source.getCopyFieldId());
        pageContext.setTargetModel(source.getTargetModel());
        pageContext.setTargetWidgetId(source.getTargetWidgetId());
        pageContext.setTargetFieldId(source.getTargetFieldId());
        pageContext.setCopyMode(source.getCopyMode());
        pageContext.setUpload(source.getUpload());
        String parentWidgetId = initWidgetId(p);
        pageContext.setParentWidgetId(parentWidgetId);
        pageContext.setParentClientWidgetId(currentClientWidgetId);
        pageContext.setParentClientPageId(pageScope != null ? pageScope.getPageId() : null);
        pageContext.setParentModelLink(actionModelLink);
        pageContext.setParentRoute(RouteUtil.addQueryParams(parentRoute, queryMapping));
        pageContext.setCloseOnSuccessSubmit(p.cast(source.getCloseAfterSubmit(), true));
        pageContext.setRefreshOnSuccessSubmit(p.cast(source.getRefreshAfterSubmit(), true));
        if (source.getRefreshWidgetId() != null && pageScope != null) {
            pageContext.setRefreshClientWidgetId(pageScope.getGlobalWidgetId(source.getRefreshWidgetId()));
        }
        pageContext.setRefreshOnClose(p.cast(source.getRefreshOnClose(), false));
        pageContext.setUnsavedDataPromptOnClose(p.cast(source.getUnsavedDataPromptOnClose(), true));
        if (source.getSubmitOperationId() != null
                && source.getRedirectUrlAfterSubmit() == null
                && Boolean.TRUE.equals(source.getFocusAfterSubmit())) {
            pageContext.setRedirectUrlOnSuccessSubmit(routeScope != null ? normalize(routeScope.getUrl() + normalize(colon("id"))) : null);
            pageContext.setRedirectTargetOnSuccessSubmit(Target.application);
        } else if (source.getRedirectUrlAfterSubmit() != null) {
            pageContext.setRedirectUrlOnSuccessSubmit(source.getRedirectUrlAfterSubmit());
            pageContext.setRedirectTargetOnSuccessSubmit(p.cast(source.getRedirectTargetAfterSubmit(),
                    RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
        }

        List<N2oPreFilter> preFilters = initPreFilters(source, actionRoute, parentWidgetId, p);
        pageContext.setPreFilters(preFilters);
        pageContext.setPathRouteMapping(pathMapping);
        // при наличии route или при filter модели не добавляем queryMapping
        if (source.getRoute() == null && !ReduxModel.FILTER.equals(actionDataModel))
            queryMapping.putAll(initPreFilterParams(preFilters, pathMapping, p));
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
                String datasource = (pageScope == null || pageScope.getWidgetIdDatasourceMap() == null)
                        ? actionDataModelClientWidgetId
                        : pageScope.getWidgetIdDatasourceMap().get(actionDataModelClientWidgetId);
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

        List<N2oParam> resultParams = prepareParams(params, actionDataModel, pageScope, defaultParamRefWidgetId, p);
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
        List<N2oParam> resultParams = prepareParams(params, actionDataModel, pageScope, defaultParamRefWidgetId, p);
        queryMapping.putAll(initParams(resultParams, pathMapping, widgetIdQueryIdMap, p));
    }

    /**
     * Подготовка параметров
     *
     * @param params                  Список входящих параметров
     * @param actionDataModel         Модель действия
     * @param pageScope               Информация о странице
     * @param defaultParamRefWidgetId Идентификатор виджета, который будет использоваться по умолчанию,
     *                                в случае, если в параметре не задан виджет
     * @param p                       Процессор сборки метаданных
     * @return Список преобразованных параметров
     */
    private List<N2oParam> prepareParams(N2oParam[] params, ReduxModel actionDataModel,
                                         PageScope pageScope, String defaultParamRefWidgetId, CompileProcessor p) {
        List<N2oParam> resultParams = new ArrayList<>();
        for (N2oParam param : params) {
            String widgetId = p.cast(param.getRefWidgetId(), defaultParamRefWidgetId);
            resultParams.add(new N2oParam(param.getName(), param.getValue(), widgetId,
                    p.cast(param.getRefModel(), actionDataModel),
                    pageScope == null ? null : pageScope.getPageId()));
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
                            if (ReduxModel.RESOLVE.equals(link.getModel()) && Objects.equals(link.getFieldId(), "id")
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
            if (isDynamicPage && actionModelLink != null && ReduxModel.RESOLVE.equals(actionModelLink.getModel())) {
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
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getWidgetId();
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                return widgetIdAware.getWidgetId();
            }
        }
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