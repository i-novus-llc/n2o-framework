package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.CopiedFieldScope;
import net.n2oapp.framework.config.metadata.compile.widget.FiltersScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientWidgetId;

/**
 * Компиляция страницы с единственным виджетом
 */
@Component
public class SimplePageCompiler extends PageCompiler<N2oSimplePage, SimplePage> {

    private static final String MAIN_WIDGET_ID = "main";

    @Override
    public SimplePage compile(N2oSimplePage source, PageContext context, CompileProcessor p) {
        SimplePage page = new SimplePage();
        String pageName = p.cast(context.getPageName(), source.getName(), source.getWidget().getName());
        page.setPageProperty(initPageName(source, pageName, context, p));
        compileBaseProperties(source, page, context, p);

        String pageRoute = initPageRoute(source, context, p);
        page.setProperties(p.mapAttributes(source));
        page.setBreadcrumb(initBreadcrumb(source, pageName, context, p));
        String refId = source.getWidget().getRefId();
        if (refId != null && !DynamicUtil.isDynamic(refId)) {
            source.setWidget(p.merge(p.getSource(refId, N2oWidget.class), source.getWidget()));
        }
        N2oWidget widget = source.getWidget();
        widget.setId(p.cast(widget.getId(), MAIN_WIDGET_ID));
        widget.setRoute(p.cast(widget.getRoute(), "/" + ("/".equals(pageRoute) ? widget.getId() : "")));
        PageScope pageScope = initPageScope(page.getId(), widget, context);
        PageRoutes routes = initRoute(pageRoute);
        ParentRouteScope pageRouteScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        BreadcrumbList breadcrumbs = new BreadcrumbList(page.getBreadcrumb());
        ValidationScope validationScope = new ValidationScope();
        CopiedFieldScope copiedFieldScope = new CopiedFieldScope();
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        DataSourcesScope datasourcesScope = new DataSourcesScope();
        FiltersScope filtersScope = new FiltersScope();
        SubModelsScope subModelsScope = new SubModelsScope();
        Widget<?> compiledWidget = p.compile(widget, context, routes, pageScope, pageRouteScope, breadcrumbs,
                validationScope, page.getModels(), pageRoutesScope, datasourcesScope, filtersScope,
                copiedFieldScope, subModelsScope);
        page.setWidget(compiledWidget);
        page.getPageProperty().setDatasource(compiledWidget.getDatasource());
        registerRoutes(routes, context, p);
        page.setRoutes(routes);
        compileComponent(page, source, context, p);
        Map<String, Widget<?>> compiledWidgets = new HashMap<>();
        compiledWidgets.put(compiledWidget.getId(), compiledWidget);
        page.setDatasources(initDatasources(datasourcesScope, context, p, pageScope, validationScope, routes,
                pageRouteScope, pageScope, filtersScope, copiedFieldScope, subModelsScope));
        page.setToolbar(compileToolbar(context, p, widget.getDatasourceId(), pageScope,
                new MetaActions(), pageRouteScope, breadcrumbs, validationScope, datasourcesScope));
        return page;
    }

    private PageScope initPageScope(String pageId, N2oWidget widget, PageContext context) {
        PageScope pageScope = new PageScope();
        pageScope.setPageId(pageId);
        pageScope.setResultWidgetId(widget.getId());
        if (widget.getDatasource() != null)
            pageScope.setObjectId(widget.getDatasource().getObjectId());
        if (widget.getDatasource() != null && widget.getDatasource().getQueryId() != null)
            pageScope.setWidgetIdQueryIdMap(Map.of(widget.getId(), widget.getDatasource().getQueryId()));
        pageScope.setWidgetIdClientDatasourceMap(new HashMap<>());
        pageScope.setWidgetIdSourceDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdSourceDatasourceMap().putAll(Map.of(widget.getId(),
                widget.getDatasourceId() == null ? widget.getId() : widget.getDatasourceId()));
        pageScope.getWidgetIdClientDatasourceMap().putAll(Map.of(getClientWidgetId(widget.getId(), pageId),
                getClientDatasourceId(widget.getDatasourceId() == null ? widget.getId() : widget.getDatasourceId(), pageId)));
        if (context.getParentWidgetIdDatasourceMap() != null)
            pageScope.getWidgetIdClientDatasourceMap().putAll(context.getParentWidgetIdDatasourceMap());
        return pageScope;
    }

    private Map<String, AbstractDatasource> initDatasources(DataSourcesScope dataSourcesScope, PageContext context,
                                                                       CompileProcessor p, PageScope pageScope, Object ... scopes) {
        Map<String, AbstractDatasource> compiledDatasources = new StrictMap<>();
        initContextDatasources(dataSourcesScope, pageScope, context, p);
        if (!dataSourcesScope.isEmpty()) {
            dataSourcesScope.values().forEach(ds -> {
                AbstractDatasource compiled = p.compile(ds, context, dataSourcesScope, scopes);
                compiledDatasources.put(compiled.getId(), compiled);
            });
        }
        return compiledDatasources;
    }

    private PageRoutes initRoute(String pageRoute) {
        PageRoutes routes = new PageRoutes();
        routes.addRoute(new PageRoutes.Route(pageRoute));
        return routes;
    }

    private Toolbar compileToolbar(PageContext context, CompileProcessor p, String datasourceId, PageScope pageScope, Object... scopes) {
        if ((context.getSubmitOperationId() != null || SubmitActionType.copy.equals(context.getSubmitActionType()))) {
            N2oToolbar n2oToolbar = new N2oToolbar();
            n2oToolbar.setGenerate(new String[]{GenerateType.submit.name(), GenerateType.close.name()});
            n2oToolbar.setDatasourceId(datasourceId);
            ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.page.toolbar.place"), String.class));
            CompiledObject object = null;
            if (pageScope.getObjectId() != null)
                object = p.getCompiled(new ObjectContext(pageScope.getObjectId()));
            return p.compile(n2oToolbar, context,
                    new IndexScope(), toolbarPlaceScope, object, pageScope, scopes);
        } else
            return null;
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
