package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.application.*;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.meta.event.Event;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция приложения
 */
@Component
public class ApplicationCompiler implements BaseSourceCompiler<Application, N2oApplication, ApplicationContext>, SourceClassAware {

    @Override
    public Application compile(N2oApplication source, ApplicationContext context, CompileProcessor p) {
        Application application = new Application();

        initWelcomePage(source, p);

        Application.Layout layout = new Application.Layout();
        layout.setFixed(castDefault(source.getNavigationLayoutFixed(),
                () -> p.resolve(property("n2o.application.navigation_layout_fixed"), Boolean.class)));

        NavigationLayoutEnum navLayout = castDefault(source.getNavigationLayout(),
                () -> p.resolve(property("n2o.application.navigation_layout"), NavigationLayoutEnum.class));
        layout.setFullSizeHeader(navLayout.equals(NavigationLayoutEnum.FULL_SIZE_HEADER));

        application.setLayout(layout);

        DataSourcesScope dataSourcesScope = initDatasourcesScope(source.getDatasources());

        Header header = initHeader(source.getHeader(), context, dataSourcesScope, p);
        application.setHeader(header);

        application.setSidebars(collectSidebars(source.getSidebars(), header, context, dataSourcesScope, p));
        application.setFooter(initFooter(source.getFooter(), p));
        application.setEvents(initEvents(source.getEvents(), context, p));
        application.setWsPrefix(initWsPrefix(application.getDatasources(), application.getEvents(), p));
        application.setDatasources(initDatasources(dataSourcesScope, context, p));
        return application;
    }

    private List<Sidebar> collectSidebars(N2oSidebar[] sidebars, Header header, ApplicationContext context,
                                          DataSourcesScope dataSourcesScope, CompileProcessor p) {
        if (sidebars == null)
            return null;

        List<Sidebar> result = new ArrayList<>();
        for (N2oSidebar n2oSidebar : sidebars) {
            Sidebar sidebar = initSidebar(n2oSidebar, header, context, dataSourcesScope, p);
            result.add(sidebar);
        }

        return result;
    }

    private DataSourcesScope initDatasourcesScope(N2oAbstractDatasource[] datasources) {
        DataSourcesScope scope = new DataSourcesScope();
        if (datasources == null)
            return scope;
        for (N2oAbstractDatasource datasource : datasources)
            scope.put(datasource.getId(), datasource);
        return scope;
    }

    private Header initHeader(N2oHeader source, ApplicationContext context, DataSourcesScope dataSourcesScope, CompileProcessor p) {
        if (source == null)
            return null;

        Header header = new Header();

        header.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.header.src"), String.class)));
        header.setClassName(source.getCssClass());
        header.setStyle(StylesResolver.resolveStyles(source.getStyle()));

        if (source.getDatasourceId() != null) {
            N2oAbstractDatasource datasource = dataSourcesScope.get(source.getDatasourceId());
            if (datasource != null)
                datasource.setSize(p.resolve(property("n2o.api.header.size"), Integer.class));
            header.setDatasource(source.getDatasourceId());
        }

        Logo logo = new Logo();
        logo.setTitle(p.resolveJS(source.getTitle()));
        logo.setSrc(source.getLogoSrc());
        logo.setHref(source.getHomePageUrl());
        header.setLogo(logo);

        ComponentScope componentScope = new ComponentScope(source);
        header.setMenu(source.getMenu() != null
                ? p.compile(source.getMenu(), context, dataSourcesScope, componentScope)
                : new SimpleMenu());
        header.setExtraMenu(source.getExtraMenu() != null
                ? p.compile(source.getExtraMenu(), context, dataSourcesScope, componentScope)
                : new SimpleMenu());
        header.setSearch(source.getSearchBar() != null
                ? p.compile(source.getSearchBar(), context)
                : null);
        header.setProperties(p.mapAttributes(source));

        if (source.getSidebarIcon() != null || source.getSidebarToggledIcon() != null) {
            Header.SidebarSwitcher sidebarSwitcher = new Header.SidebarSwitcher();
            sidebarSwitcher.setIcon(source.getSidebarIcon());
            sidebarSwitcher.setToggledIcon(castDefault(source.getSidebarToggledIcon(), source.getSidebarIcon()));
            header.setSidebarSwitcher(sidebarSwitcher);
        }

        return header;
    }

    private Sidebar initSidebar(N2oSidebar source, Header header, ApplicationContext context, DataSourcesScope dataSourcesScope, CompileProcessor p) {
        if (source == null)
            return null;

        Sidebar sidebar = p.compile(source, context, dataSourcesScope);
        if (header != null && header.getSidebarSwitcher() != null) {
            sidebar.setDefaultState(castDefault(source.getDefaultState(), SidebarStateEnum.NONE));
            sidebar.setToggledState(castDefault(source.getToggledState(), SidebarStateEnum.MAXI));
        }

        return sidebar;
    }

    private Footer initFooter(N2oFooter source, CompileProcessor p) {
        if (source == null)
            return null;

        Footer footer = new Footer();
        footer.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.footer.src"), String.class)));
        footer.setClassName(source.getCssClass());
        footer.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        footer.setTextLeft(p.resolveJS(source.getLeftText()));
        footer.setTextRight(p.resolveJS(source.getRightText()));
        footer.setProperties(p.mapAttributes(source));

        return footer;
    }

    private void initWelcomePage(N2oApplication source, CompileProcessor p) {
        String welcomePageId = castDefault(source.getWelcomePageId(),
                () -> p.resolve(property("n2o.homepage.id"), String.class));
        PageContext context = new PageContext(welcomePageId, "/");
        p.addRoute(context);
    }

    private Map<String, AbstractDatasource> initDatasources(DataSourcesScope dataSourcesScope, ApplicationContext context,
                                                            CompileProcessor p) {
        Map<String, AbstractDatasource> result = new HashMap<>();
        dataSourcesScope.values().forEach(ds -> {
            AbstractDatasource compiled = p.compile(ds, context);
            result.put(compiled.getId(), compiled);
        });
        return result;
    }

    private List<Event> initEvents(N2oAbstractEvent[] source, ApplicationContext context, CompileProcessor p) {
        if (source == null)
            return null;

        List<Event> events = new ArrayList<>();
        for (N2oAbstractEvent e : source)
            events.add(p.compile(e, context));

        return events;
    }

    private String initWsPrefix(Map<String, AbstractDatasource> datasources, List<Event> events, CompileProcessor p) {
        boolean containsStompDs = datasources != null && datasources.values().stream().anyMatch(StompDatasource.class::isInstance);
        if (!containsStompDs && events == null)
            return null;
        return p.resolve(property("n2o.config.ws.endpoint"), String.class);
    }

    @Override
    public Class<N2oApplication> getSourceClass() {
        return N2oApplication.class;
    }
}
