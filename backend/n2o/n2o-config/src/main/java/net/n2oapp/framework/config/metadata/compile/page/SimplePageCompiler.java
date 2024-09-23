package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.ClientDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.widget.CopiedFieldScope;
import net.n2oapp.framework.config.metadata.compile.widget.FiltersScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Компиляция страницы с единственным виджетом
 */
@Component
public class SimplePageCompiler extends PageCompiler<N2oSimplePage, SimplePage> {

    private static final String MAIN_WIDGET_ID = "w1";

    @Override
    public SimplePage compile(N2oSimplePage source, PageContext context, CompileProcessor p) {
        SimplePage page = new SimplePage();
        String pageName = castDefault(context.getPageName(), source.getName());
        page.setPageProperty(initPageName(source, pageName, context, p));
        compileBaseProperties(source, page, context, p);
        compileComponent(page, source, p);
        page.setProperties(p.mapAttributes(source));
        page.setBreadcrumb(initBreadcrumb(source, pageName, context, p));

        String refId = source.getWidget().getRefId();
        if (refId != null && !DynamicUtil.isDynamic(refId)) {
            source.setWidget(p.merge(p.getSource(refId, N2oWidget.class), source.getWidget()));
        }

        N2oWidget widget = source.getWidget();
        widget.setId(castDefault(widget.getId(), MAIN_WIDGET_ID));
        String pageRoute = initPageRoute(source, context, p);

        PageRoutes routes = initRoute(pageRoute);
        ParentRouteScope pageRouteScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        BreadcrumbList breadcrumbs = new BreadcrumbList(page.getBreadcrumb());
        ValidationScope validationScope = new ValidationScope();
        CopiedFieldScope copiedFieldScope = new CopiedFieldScope();

        DataSourcesScope datasourcesScope = new DataSourcesScope();
        ClientDatasourceIdsScope clientDatasourceIdsScope = new ClientDatasourceIdsScope();

        PageScope pageScope = initPageScope(page.getId(), widget, context, p);
        PageIndexScope pageIndexScope = new PageIndexScope(page.getId());
        FiltersScope filtersScope = new FiltersScope();
        SubModelsScope subModelsScope = new SubModelsScope();

        Widget<?> compiledWidget = p.compile(widget, context, routes, pageScope, pageRouteScope, breadcrumbs,
                validationScope, page.getModels(), pageRoutesScope, datasourcesScope, clientDatasourceIdsScope, filtersScope, copiedFieldScope, subModelsScope, pageIndexScope);
        initContextDatasources(datasourcesScope, clientDatasourceIdsScope, pageScope, context, p);
        page.setDatasources(initDatasources(datasourcesScope, context, p,
                clientDatasourceIdsScope, pageScope, validationScope, routes,
                pageRouteScope, pageScope, filtersScope, copiedFieldScope, subModelsScope));

        page.setWidget(compiledWidget);
        page.getPageProperty().setDatasource(compiledWidget.getDatasource());

        registerRoutes(routes, context, p);
        page.setRoutes(routes);

        page.setToolbar(compileToolbar(context, widget, p, pageScope,
                new MetaActions(), pageRouteScope, breadcrumbs, validationScope, datasourcesScope,
                clientDatasourceIdsScope, pageIndexScope));
        return page;
    }

    private Toolbar compileToolbar(PageContext context, N2oWidget resultWidget, CompileProcessor p,
                                   PageScope pageScope, Object... scopes) {
        if (isEmpty(context.getToolbars()))
            return null;

        Toolbar result = new Toolbar();
        IndexScope indexScope = new IndexScope();
        // клонируем тулбары из контекста в тулбары страницы, это необходимо, чтобы проставить значения по умолчанию
        // для datasource и при это не испортить контекст изменениями
        context.getToolbars().forEach(t -> {
            N2oToolbar toolbar = cloneToolbar(t, resultWidget, p);
            CompiledObject object = null;
            if (pageScope.getObjectId() != null)
                object = p.getCompiled(new ObjectContext(pageScope.getObjectId()));
            Toolbar compiledToolbar = p.compile(toolbar, context, indexScope, object, pageScope, scopes);
            result.putAll(compiledToolbar);
        });

        return result;
    }

    private PageScope initPageScope(String pageId, N2oWidget widget, PageContext context, CompileProcessor p) {
        PageScope pageScope = new PageScope();
        pageScope.setPageId(pageId);
        pageScope.setResultWidgetId(widget.getId());
        if (widget.getDatasource() != null)
            pageScope.setObjectId(widget.getDatasource().getObjectId());
        pageScope.setWidgetIdSourceDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdSourceDatasourceMap().put(widget.getId(),
                widget.getDatasourceId() == null ? widget.getId() : widget.getDatasourceId());
        return pageScope;
    }

    private Map<String, AbstractDatasource> initDatasources(DataSourcesScope dataSourcesScope,
                                                            PageContext context,
                                                            CompileProcessor p, Object... scopes) {
        Map<String, AbstractDatasource> compiledDatasources = new HashMap<>();
        if (!dataSourcesScope.isEmpty()) {
            dataSourcesScope.values().stream()
                    .filter(ds -> !(ds instanceof N2oApplicationDatasource || ds instanceof N2oParentDatasource))
                    .forEach(ds -> {
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

    @Override
    public Class<N2oSimplePage> getSourceClass() {
        return N2oSimplePage.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.simple.src";
    }
}
