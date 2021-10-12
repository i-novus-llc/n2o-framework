package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция страницы с единственным виджетом
 */
@Component
public class SimplePageCompiler extends PageCompiler<N2oSimplePage, SimplePage> {

    private static final String MAIN_WIDGET_ID = "main";

    @Override
    public SimplePage compile(N2oSimplePage source, PageContext context, CompileProcessor p) {
        SimplePage page = new SimplePage();
        String pageRoute = initPageRoute(source, context, p);
        page.setId(p.cast(context.getClientPageId(), RouteUtil.convertPathToId(pageRoute)));
        PageScope pageScope = new PageScope();
        //todo когда появится object-id у simple-page необходимо его и id главного виджета добавить в PageScope
        pageScope.setPageId(page.getId());
        String pageName = p.cast(context.getPageName(), source.getName(), source.getWidget().getName());
        page.setPageProperty(initPageName(source, pageName, context, p));
        page.setProperties(p.mapAttributes(source));
        page.setBreadcrumb(initBreadcrumb(pageName, context, p));
        String refId = source.getWidget().getRefId();
        if (refId != null && !DynamicUtil.isDynamic(refId)) {
            source.setWidget(p.merge(p.getSource(refId, N2oWidget.class), source.getWidget()));
        }
        N2oWidget widget = source.getWidget();
        widget.setId(p.cast(widget.getId(), MAIN_WIDGET_ID));
        widget.setRoute(p.cast(widget.getRoute(), "/" + ("/".equals(pageRoute) ? widget.getId() : "")));
        if (widget.getQueryId() != null)
            pageScope.setWidgetIdQueryIdMap(Map.of(widget.getId(), widget.getQueryId()));
        pageScope.setWidgetIdDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdDatasourceMap().putAll(Map.of(pageScope.getGlobalWidgetId(widget.getId()),
                pageScope.getGlobalWidgetId(widget.getDatasource() == null ? widget.getId() : widget.getDatasource())));
        if (context.getParentWidgetIdDatasourceMap() != null)
            pageScope.getWidgetIdDatasourceMap().putAll(context.getParentWidgetIdDatasourceMap());
        PageRoutes routes = initRoute(pageRoute);
        initPreFilters(context, widget);
        Models models = new Models();
        page.setModels(models);
        WidgetScope widgetScope = new WidgetScope();
        ParentRouteScope pageRouteScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        BreadcrumbList breadcrumbs = new BreadcrumbList(page.getBreadcrumb());
        ValidationList validationList = new ValidationList(new HashMap<>());
        if (context.getUpload() != null)
            widget.setUpload(context.getUpload());
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        Widget compiledWidget = p.compile(widget, context, routes, pageScope, widgetScope, pageRouteScope, breadcrumbs, validationList, models, pageRoutesScope);
        page.setWidget(compiledWidget);
        registerRoutes(routes, context, p);
        page.setRoutes(routes);
        compileComponent(page, source, context, p);
        String objectId = p.cast(source.getObjectId(), compiledWidget.getObjectId());
        CompiledObject object = null;
        if (objectId != null) {
            object = p.getCompiled(new ObjectContext(objectId));
            page.setObject(object);
        }
        if ((context.getSubmitOperationId() != null || SubmitActionType.copy.equals(context.getSubmitActionType()))) {
            MetaActions metaActions = new MetaActions();
            page.setToolbar(compileToolbar(context, p, metaActions, pageScope, pageRouteScope, object, breadcrumbs, validationList, widget));
            compiledWidget.getActions().putAll(metaActions);
        }
        return page;
    }

    private PageRoutes initRoute(String pageRoute) {
        PageRoutes routes = new PageRoutes();
        routes.addRoute(new PageRoutes.Route(pageRoute));
        return routes;
    }

    private void initPreFilters(PageContext context, N2oWidget widget) {
        if (context.getPreFilters() != null && !context.getPreFilters().isEmpty()) {
            widget.addPreFilters(context.getPreFilters());
        }
    }

    private Toolbar compileToolbar(PageContext context, CompileProcessor p,
                                   MetaActions metaActions, PageScope pageScope, ParentRouteScope routeScope,
                                   CompiledObject object, BreadcrumbList breadcrumbs, ValidationList validationList,
                                   N2oWidget widget) {
        N2oToolbar n2oToolbar = new N2oToolbar();
        n2oToolbar.setGenerate(new String[]{GenerateType.submit.name(), GenerateType.close.name()});
        n2oToolbar.setTargetWidgetId(p.cast(widget.getId(), MAIN_WIDGET_ID));
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.page.toolbar.place"), String.class));
        return p.compile(n2oToolbar, context, metaActions, pageScope, routeScope, object,
                new IndexScope(), breadcrumbs, validationList, toolbarPlaceScope);
    }

    @Override
    public Class<N2oSimplePage> getSourceClass() {
        return N2oSimplePage.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.simple.src";
    }
}
