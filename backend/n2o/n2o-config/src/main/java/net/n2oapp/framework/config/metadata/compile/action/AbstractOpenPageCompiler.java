package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.global.dao.N2oQuery.Field.PK;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Абстрактная реализация компиляция open-page, show-modal
 */
@Component
public abstract class AbstractOpenPageCompiler<D extends AbstractAction, S extends N2oAbstractPageAction> extends AbstractActionCompiler<D, S> {

    protected List<N2oPreFilter> initPreFilters(N2oAbstractPageAction source, String masterIdParam,
                                                CompileProcessor p) {
        List<N2oPreFilter> preFilters = new ArrayList<>();
        ReduxModel model = ReduxModel.RESOLVE;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String widgetId = widgetScope == null ? null : widgetScope.getWidgetId();
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                model = modelAware.getModel();
            }
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
                filter.setParam(p.cast(masterIdParam, filter.getFieldId()));
            } else {
                filter.setParam(filter.getFieldId());
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
                filter.setParam(filter.getFieldId());
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
        String pageId = source.getPageId();
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String route = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute(p), "");
        Map<String, ModelLink> pathMapping = new StrictMap<>();
        Map<String, ModelLink> queryMapping = new StrictMap<>();
        if (routeScope != null) {
            pathMapping.putAll(routeScope.getPathMapping());
            queryMapping.putAll(routeScope.getQueryMapping());
        }

        String currentClientWidgetId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            currentClientWidgetId = widgetScope.getClientWidgetId();
        }

        ModelLink actionModelLink = null;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            ReduxModel actionDataModel = ReduxModel.RESOLVE;
            if (modelAware != null && modelAware.getModel() != null) {
                actionDataModel = modelAware.getModel();
            }
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            String actionDataModelClientWidgetId = null;
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                PageScope pageScope = p.getScope(PageScope.class);
                actionDataModelClientWidgetId = pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());
            } else {
                actionDataModelClientWidgetId = currentClientWidgetId;
            }
            if (actionDataModel != null && actionDataModelClientWidgetId != null)
                actionModelLink = new ModelLink(actionDataModel, actionDataModelClientWidgetId);
        }
        if (actionModelLink == null)
            throw new N2oException("widget-id for action " + source.getId() + " not specified");

        if (currentClientWidgetId == null)
            currentClientWidgetId = actionModelLink.getWidgetId();

        String actionRoute = initActionRoute(source, actionModelLink);
        String masterIdParam = initMasterLink(actionRoute, pathMapping, actionModelLink, p);
        route = normalize(route + actionRoute);
        String parentRoute = RouteUtil.absolute("../", route);// example "/:id/action" -> "/:id"

        PageContext pageContext = constructContext(pageId, route);
        pageContext.setPageName(source.getPageName());
        pageContext.setBreadcrumbs(p.getScope(BreadcrumbList.class));
        pageContext.setSubmitOperationId(source.getSubmitOperationId());
        pageContext.setSubmitLabel(source.getSubmitLabel());
        pageContext.setSubmitModel(source.getSubmitModel());
        pageContext.setResultWidgetId(source.getResultContainerId());
        pageContext.setUpload(source.getUpload());
        pageContext.setParentWidgetId(currentClientWidgetId);
        pageContext.setParentModelLink(actionModelLink);
        pageContext.setParentRoute(RouteUtil.addQueryParams(parentRoute, queryMapping.keySet()));
        pageContext.setCloseOnSuccessSubmit(p.cast(source.getCloseAfterSubmit(), true));
        pageContext.setRefreshOnSuccessSubmit(p.cast(source.getRefreshAfterSubmit(), true));
        pageContext.setRefreshOnClose(p.cast(source.getRefreshOnClose(), false));
        if (source.getSubmitOperationId() != null
                && source.getRedirectUrlAfterSubmit() == null
                && source.getFocusAfterSubmit() != null
                && source.getFocusAfterSubmit()) {
            pageContext.setRedirectUrlOnSuccessSubmit(routeScope != null ? routeScope.getUrl() + normalize(colon("id")) : null);
            pageContext.setRedirectTargetOnSuccessSubmit(Target.application);
        } else if (source.getRedirectUrlAfterSubmit() != null) {
            pageContext.setRedirectUrlOnSuccessSubmit(source.getRedirectUrlAfterSubmit());
            pageContext.setRedirectTargetOnSuccessSubmit(p.cast(source.getRedirectTargetAfterSubmit(),
                    RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
        }

        List<N2oPreFilter> preFilters = initPreFilters(source, masterIdParam, p);
        pageContext.setPreFilters(preFilters);
        pageContext.setPathRouteMapping(pathMapping);
        queryMapping.putAll(initPreFilterParams(preFilters, pathMapping, queryMapping));
        pageContext.setQueryRouteMapping(queryMapping);

        initPageRoute(compiled, route, pathMapping, queryMapping);
        initOtherPageRoute(p, route);
        p.addRoute(route, pageContext);
        return pageContext;
    }

    /**
     * Инициализация ссылки на идентификатор модели текущего виджета
     *
     * @param actionRoute Маршрут с параметром
     * @param pathMapping Параметры, в которые добавится ссылка
     * @param actionModelLink Модель данных действия
     * @param p
     *
     * @return Наименование параметра ссылки
     */
    private String initMasterLink(String actionRoute, Map<String, ModelLink> pathMapping, ModelLink actionModelLink,
                                  CompileProcessor p) {
        List<String> actionRouteParams = RouteUtil.getParams(actionRoute);
        String masterIdParam = null;
        if (!actionRouteParams.isEmpty()) {
            if (actionModelLink == null)
                throw new N2oException("Action route contains params " + actionRoute + ", but parent widget not found");
            if (actionRouteParams.size() > 1)
                throw new N2oException("Action route can not contain more then one param: " + actionRoute);
            masterIdParam = actionRouteParams.get(0);
            CompiledQuery query = p.getScope(CompiledQuery.class);//todo для кнопок страницы будет null
            String queryId = query != null ? query.getId() : null;
            ModelLink masterLink = Redux.linkQuery(actionModelLink.getWidgetId(), PK, queryId);
            pathMapping.put(masterIdParam, masterLink);
        }
        return masterIdParam;
    }

    /**
     * Добавление идентификатора текущего виджета в параметры маршрута.
     * Если текущий виджет и виджет из модели действия совпадают и модель resolve, то добавляем.
     *
     * @param source                Действие
     * @param actionModelLink       Ссылка на модель действия
     * @return Маршрут с добавкой идентификатора или без
     */
    private String initActionRoute(S source, ModelLink actionModelLink) {
        String actionRoute = source.getRoute();
        if (actionRoute == null) {
            actionRoute = normalize(source.getId());
            if (actionModelLink != null
                    && ReduxModel.RESOLVE.equals(actionModelLink.getModel())) {
                String masterIdParam = actionModelLink.getWidgetId() + "_id";
                actionRoute = normalize(colon(masterIdParam)) + actionRoute;
            }
        }
        return actionRoute;
    }

    protected abstract void initPageRoute(D compiled, String route,
                                          Map<String, ModelLink> pathMapping,
                                          Map<String, ModelLink> queryMapping);

    private void initOtherPageRoute(CompileProcessor p, String route) {
        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null) {
            PageRoutes.Route pageRoute = new PageRoutes.Route(route);
            pageRoute.setIsOtherPage(true);
            pageRoutes.addRoute(pageRoute);
        }
    }


    private Map<String, ModelLink> initPreFilterParams(List<N2oPreFilter> preFilters,
                                                       Map<String, ModelLink> pathParams,
                                                       Map<String, ModelLink> queryParams) {
        if (preFilters == null) return null;
        Map<String, ModelLink> res = new HashMap<>();
        Set<String> params = new HashSet<>();
        params.addAll(pathParams.keySet());
        params.addAll(queryParams.keySet());
        preFilters.stream().filter(f -> f.getParam() != null && !params.contains(f.getParam()))
                .forEach(f -> res.put(f.getParam(), Redux.linkFilter(f)));
        return res;
    }
}
