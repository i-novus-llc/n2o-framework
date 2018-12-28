package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionOptions;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRoteScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Компиляция open-page
 */
@Component
public class OpenPageCompiler extends AbstractOpenPageCompiler<LinkAction, N2oOpenPage> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOpenPage.class;
    }

    @Override
    public LinkAction compile(N2oOpenPage source, CompileContext<?, ?> context, CompileProcessor p) {
        LinkAction openPage = new LinkAction(new LinkActionOptions());
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.link.src"), String.class)));
        openPage.setObjectId(source.getObjectId());
        openPage.setOperationId(source.getOperationId());
        openPage.setPageId(source.getPageId());
        compileAction(openPage, source, p);
        initBreadcrumb(openPage, source, context, p);
        return openPage;
    }

    protected void initBreadcrumb(LinkAction compiled, N2oOpenPage source, CompileContext<?, ?> context, CompileProcessor p) {
        Map<String, BindLink> pathMapping = new StrictMap<>();
        ParentRoteScope routeScope = p.getScope(ParentRoteScope.class);
        String route = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute(p), "");
        Map<String, ModelLink> modelLinkMap = new HashMap<>(routeScope.getPathMapping());
        String pageId = source.getPageId();
        ReduxModel widgetModel = ReduxModel.RESOLVE;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                widgetModel = modelAware.getModel();
            }
        }
        boolean contextAction = ReduxModel.RESOLVE.equals(widgetModel);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String masterIdFilter = null;
        if (widgetScope != null && contextAction) {
            masterIdFilter = widgetScope.getClientWidgetId() + "_id";
            route = route + normalize(colon(masterIdFilter));
            ModelLink link = new ModelLink(ReduxModel.RESOLVE, widgetScope.getClientWidgetId(), "id", masterIdFilter);
            pathMapping.put(masterIdFilter, link);
            modelLinkMap.put(masterIdFilter, link);
        }
        String parentRoute = route;//специально на этом месте, потому что дальше route дополняется и это не должно быть в parentRoute
        route = normalize(route + normalize(p.cast(source.getRoute(), source.getId())));
        PageContext openPageContext = new PageContext(pageId, route);
        openPageContext.setPageName(source.getPageName());
        openPageContext.setBreadcrumbs(p.getScope(BreadcrumbList.class));
        openPageContext.setSubmitOperationId(source.getSubmitOperationId());
        openPageContext.setSubmitLabel(source.getSubmitLabel());
        openPageContext.setSubmitModel(source.getSubmitModel());
        openPageContext.setResultWidgetId(source.getResultContainerId());
        openPageContext.setUpload(source.getUpload());
        openPageContext.setParentWidgetId(widgetScope != null ? widgetScope.getClientWidgetId() : null);//todo для page actions какой parentWidgetId?
        openPageContext.setCloseOnSuccessSubmit(p.cast(source.getCloseAfterSubmit(), true));
        openPageContext.setRefreshOnSuccessSubmit(p.cast(source.getRefreshAfterSubmit(), true));
        openPageContext.setRefreshOnClose(p.cast(source.getRefreshOnClose(), false));
        if (routeScope != null
                && source.getSubmitOperationId() != null
                && source.getRedirectUrlAfterSubmit() == null
                && source.getFocusAfterSubmit() != null
                && source.getFocusAfterSubmit()) {
            openPageContext.setRedirectUrlOnSuccessSubmit(routeScope.getUrl() + normalize(colon("id")));
            openPageContext.setRedirectTargetOnSuccessSubmit(Target.application);
        } else if (source.getRedirectUrlAfterSubmit() != null) {
            openPageContext.setRedirectUrlOnSuccessSubmit(source.getRedirectUrlAfterSubmit());
            openPageContext.setRedirectTargetOnSuccessSubmit(p.cast(source.getRedirectTargetAfterSubmit(),
                    RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
        }
        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null) {
            PageRoutes.Route pageRoute = new PageRoutes.Route(route);
            pageRoute.setIsOtherPage(true);
            pageRoutes.addRoute(pageRoute);
        }
        compiled.getOptions().setPath(route);
        compiled.getOptions().setTarget(Target.application);
        if (routeScope != null && routeScope.getPathMapping() != null) {
            for (String pathParam : routeScope.getPathMapping().keySet()) {
                BindLink bindLink = routeScope.getPathMapping().get(pathParam);
                pathMapping.put(pathParam, bindLink);
            }
        }
        compiled.getOptions().setPathMapping(pathMapping);
        Set<String> pathParams = pathMapping.keySet();
        List<N2oPreFilter> preFilters = initPreFilters(source, masterIdFilter, widgetScope, p);
        openPageContext.setQueryRouteInfos(initRouteInfos(preFilters, pathParams, p));
        openPageContext.setPathRouteInfos(modelLinkMap);
        openPageContext.setPreFilters(preFilters);
        Map<String, BindLink> queryMapping = new StrictMap<>();
        preFilters.stream().filter(n2oPreFilter -> n2oPreFilter.getParam() != null && !pathParams.contains(n2oPreFilter.getParam()))
                .forEach(preFilter -> queryMapping.put(preFilter.getParam(), createModelLink(preFilter, null, preFilter.getParam(), p)));
        compiled.getOptions().setQueryMapping(queryMapping);
        parentRoute = RouteUtil.addQueryParams(parentRoute, queryMapping.keySet());
        openPageContext.setParentRoute(parentRoute);
        p.addRoute(route, openPageContext);
    }
}
