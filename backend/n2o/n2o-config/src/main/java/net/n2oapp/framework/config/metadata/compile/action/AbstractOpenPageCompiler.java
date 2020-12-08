package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.global.dao.N2oQuery.Field.PK;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Абстрактная реализация компиляция open-page, show-modal
 */
@Component
public abstract class AbstractOpenPageCompiler<D extends Action, S extends N2oAbstractPageAction> extends AbstractActionCompiler<D, S> {

    protected List<N2oPreFilter> initPreFilters(N2oAbstractPageAction source, String masterIdParam,
                                                CompileProcessor p) {
        List<N2oPreFilter> preFilters = new ArrayList<>();
        ReduxModel model = getTargetWidgetModel(p, ReduxModel.RESOLVE);
        String widgetId = initWidgetId(p);
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
                filter.setParam(p.cast(source.getMasterParam(), masterIdParam, createGlobalParam(filter.getFieldId(), p)));
            } else {
                filter.setParam(p.cast(source.getMasterParam(), createGlobalParam(filter.getFieldId(), p)));
            }
            filter.setRefModel(ReduxModel.RESOLVE);
            PageScope pageScope = p.getScope(PageScope.class);
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
                PageScope pageScope = p.getScope(PageScope.class);
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
        String currentWidgetQueryId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            currentClientWidgetId = widgetScope.getClientWidgetId();
            currentWidgetQueryId = widgetScope.getQueryId();
        }

        ModelLink actionModelLink = null;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            String actionDataModelClientWidgetId = null;
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                actionDataModelClientWidgetId = pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());
            } else {
                actionDataModelClientWidgetId = currentClientWidgetId;
            }
            if (actionDataModel != null && actionDataModelClientWidgetId != null) {
                if (currentClientWidgetId != null && currentClientWidgetId.equals(actionDataModelClientWidgetId)
                        && ReduxModel.RESOLVE.equals(actionDataModel)) {
                    //Получаем query для actionModelLink
                    actionModelLink = Redux.linkQuery(actionDataModelClientWidgetId, N2oQuery.Field.PK, currentWidgetQueryId);
                } else {
                    actionModelLink = new ModelLink(actionDataModel, actionDataModelClientWidgetId, N2oQuery.Field.PK);
                }
            }
        }
        if (actionModelLink == null)
            throw new N2oException("widget-id for action " + source.getId() + " not specified");

        if (currentClientWidgetId == null)
            currentClientWidgetId = actionModelLink.getWidgetId();

        String actionRoute = initActionRoute(source, actionModelLink);
        String masterIdParam = initMasterLink(source.getPathParams(), actionRoute, pathMapping, actionModelLink);
        addPathMappings(source, pathMapping, widgetScope, pageScope, actionDataModel, p);
        String parentRoute = normalize(route);
        List<String> pathParams = RouteUtil.getPathParams(actionRoute);
        if (!pathParams.isEmpty())
            parentRoute = normalize(parentRoute + "/:" + pathParams.get(0));
        route = normalize(route + actionRoute);

        PageContext pageContext = constructContext(pageId, route);
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
        pageContext.setResultWidgetId(source.getResultContainerId());
        pageContext.setUpload(source.getUpload());
        pageContext.setParentWidgetId(initWidgetId(p));
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
                && source.getFocusAfterSubmit() != null
                && source.getFocusAfterSubmit()) {
            pageContext.setRedirectUrlOnSuccessSubmit(routeScope != null ? normalize(routeScope.getUrl() + normalize(colon("id"))) : null);
            pageContext.setRedirectTargetOnSuccessSubmit(Target.application);
        } else if (source.getRedirectUrlAfterSubmit() != null) {
            pageContext.setRedirectUrlOnSuccessSubmit(source.getRedirectUrlAfterSubmit());
            pageContext.setRedirectTargetOnSuccessSubmit(p.cast(source.getRedirectTargetAfterSubmit(),
                    RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
        }

        List<N2oPreFilter> preFilters = initPreFilters(source, masterIdParam, p);
        pageContext.setPreFilters(preFilters);
        pageContext.setPathRouteMapping(pathMapping);
        // при наличии route или при filter модели не добавляем queryMapping
        if (source.getRoute() == null && !ReduxModel.FILTER.equals(actionDataModel))
            queryMapping.putAll(initPreFilterParams(preFilters, pathMapping));
        initQueryMapping(source, p, actionDataModel, pathMapping, queryMapping, pageScope, widgetScope, componentScope);
        pageContext.setQueryRouteMapping(queryMapping);

        initPageRoute(compiled, route, pathMapping, queryMapping);
        initOtherPageRoute(p, context, route);
        p.addRoute(pageContext);
        return pageContext;
    }

    private void initQueryMapping(S source, CompileProcessor p, ReduxModel actionDataModel,
                                  Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping,
                                  PageScope pageScope, WidgetScope widgetScope, ComponentScope componentScope) {
        if (source.getQueryParams() == null && source.getMasterParam() == null && source.getDetailFieldId() == null)
            return;

        List<N2oParam> queryParams = source.getQueryParams() != null ?
                new ArrayList<>(Arrays.asList(source.getQueryParams())) :
                new ArrayList<>();

        // Deprecated, будет убрана в будущем
        if (ReduxModel.RESOLVE.equals(actionDataModel) && source.getRoute() == null) {
            Consumer<String> addParamConsumer = s -> {
                N2oParam masterParam = new N2oParam();
                masterParam.setName(s);
                masterParam.setValue(Placeholders.ref(s));
                queryParams.add(masterParam);
            };
            if (source.getMasterParam() != null)
                addParamConsumer.accept(source.getMasterParam());
            if (source.getDetailFieldId() != null)
                addParamConsumer.accept(source.getDetailFieldId());
        }

        List<N2oParam> resultParams = new ArrayList<>();
        for (N2oParam param : queryParams) {
            // widget id priority : queryParam widgetId
            String widgetId = param.getRefWidgetId();
            // or else button's widgetId
            if (widgetId == null) {
                WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
                if (widgetIdAware != null)
                    widgetId = widgetIdAware.getWidgetId();
                // or else current widget's id
                if (widgetId == null && widgetScope != null)
                    widgetId = widgetScope.getWidgetId();
            }
            resultParams.add(new N2oParam(param.getName(), param.getValue(), widgetId,
                    p.cast(param.getRefModel(), actionDataModel),
                    pageScope == null ? null : pageScope.getPageId()));
        }
        queryMapping.putAll(initParams(resultParams, pathMapping));
    }

    /**
     * Инициализация ссылки на идентификатор модели текущего виджета
     *
     * @param pathParams      Параметры пути
     * @param actionRoute     Маршрут с параметром
     * @param pathMapping     Параметры, в которые добавится ссылка
     * @param actionModelLink Модель данных действия
     * @return Наименование параметра ссылки
     */
    private String initMasterLink(N2oParam[] pathParams, String actionRoute, Map<String, ModelLink> pathMapping, ModelLink actionModelLink) {
        List<String> actionRouteParams = RouteUtil.getParams(actionRoute);
        String masterIdParam = null;
        if (!actionRouteParams.isEmpty()) {
            masterIdParam = actionRouteParams.get(0);

            if (pathParams != null) {
                String finalMasterIdParam = masterIdParam;
                Optional<N2oParam> pathParam = Arrays.stream(pathParams)
                        .filter(p -> finalMasterIdParam.equals(p.getName())).findFirst();
                if (pathParam.isPresent()) {
                    String value = pathParam.get().getValue();
                    if (StringUtils.isLink(value)) {
                        SubModelQuery subModelQuery = actionModelLink.getSubModelQuery();
                        actionModelLink = new ModelLink(actionModelLink.getModel(),
                                actionModelLink.getWidgetId(),
                                value.substring(1, value.length() - 1));
                        actionModelLink.setSubModelQuery(subModelQuery);
                    } else actionModelLink = new ModelLink(value);
                }
            }
            pathMapping.put(masterIdParam, actionModelLink);
        }
        return masterIdParam;
    }

    /**
     * Добавление идентификатора текущего виджета в параметры маршрута.
     * Если текущий виджет и виджет из модели действия совпадают и модель resolve, то добавляем.
     *
     * @param source          Действие
     * @param actionModelLink Ссылка на модель действия
     * @return Маршрут с добавкой идентификатора или без
     */
    private String initActionRoute(S source, ModelLink actionModelLink) {
        String actionRoute = source.getRoute();
        if (actionRoute == null) {
            actionRoute = normalize(source.getId());
            if (StringUtils.hasLink(source.getPageId()) && actionModelLink != null && ReduxModel.RESOLVE.equals(actionModelLink.getModel())) {
                String masterIdParam = actionModelLink.getWidgetId() + "_id";
                actionRoute = normalize(colon(masterIdParam)) + actionRoute;
            }
        }
        return actionRoute;
    }

    /**
     * Добавление path-param в pathMapping
     */
    private void addPathMappings(S source, Map<String, ModelLink> pathMapping, WidgetScope widgetScope,
                                 PageScope pageScope, ReduxModel actionDataModel, CompileProcessor p) {
        if (source.getPathParams() != null) {
            List<N2oParam> params = new ArrayList<>();
            for (N2oParam param : source.getPathParams()) {
                params.add(new N2oParam(param.getName(), param.getValue(),
                        p.cast(param.getRefWidgetId(), widgetScope == null ? null : widgetScope.getWidgetId()),
                        p.cast(param.getRefModel(), actionDataModel),
                        pageScope == null ? null : pageScope.getPageId()));
            }
            pathMapping.putAll(initParams(params, pathMapping));
        }
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
                                                       Map<String, ModelLink> pathParams) {
        return preFilters == null ? null :
                preFilters.stream().filter(f -> f.getParam() != null && !pathParams.keySet().contains(f.getParam()))
                        .collect(Collectors.toMap(N2oPreFilter::getParam, Redux::linkParam));
    }

    private Map<String, ModelLink> initParams(List<N2oParam> params,
                                              Map<String, ModelLink> pathParams) {
        return params == null ? null :
                params.stream().filter(f -> f.getName() != null && !pathParams.keySet().contains(f.getName()))
                        .collect(Collectors.toMap(N2oParam::getName, Redux::linkParam));
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