package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.framework.api.metadata.application.*;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
        layout.setFixed(p.cast(source.getNavigationLayoutFixed(), true));
        layout.setFullSizeHeader(source.getNavigationLayout() == null || source.getNavigationLayout().equals(NavigationLayout.fullSizeHeader) ? true : false);
        application.setLayout(layout);

        application.setHeader(initHeader(source.getHeader(), context, p));
        application.setSidebar(initSidebar(source.getSidebar(), context, p));
        application.setFooter(initFooter(source.getFooter(), context, p));

        return application;
    }

    private Header initHeader(N2oHeader source, ApplicationContext context, CompileProcessor p) {
        if (source == null) return null;
        Header header = new Header();
        header.setSrc(source.getSrc());
        header.setClassName(source.getCssClass());
        header.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        Logo logo = new Logo();
        logo.setTitle(source.getTitle());
        logo.setSrc(source.getLogoSrc());
        logo.setHref(source.getHomePageUrl());
        header.setLogo(logo);
        header.setMenu(source.getMenu() != null ? p.compile(source.getMenu(), context) : new SimpleMenu());
        header.setExtraMenu(source.getExtraMenu() != null ? p.compile(source.getExtraMenu(), context) : new SimpleMenu());
        header.setSearch(source.getSearchBar() != null ? p.compile(source.getSearchBar(), context) : null);
        if (source.getSidebarIcon() != null || source.getSidebarToggledIcon() != null) {
            Header.SidebarSwitcher sidebarSwitcher = new Header.SidebarSwitcher();
            sidebarSwitcher.setIcon(source.getSidebarIcon());
            sidebarSwitcher.setToggledIcon(source.getSidebarToggledIcon());
            header.setSidebarSwitcher(sidebarSwitcher);
        }
        return header;
    }

    private Sidebar initSidebar(N2oSidebar source, ApplicationContext context, CompileProcessor p) {
        if (source == null) return null;
        Sidebar sidebar = new Sidebar();
        sidebar.setSrc(source.getSrc());
        sidebar.setClassName(source.getCssClass());
        sidebar.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        Logo logo = new Logo();
        logo.setTitle(source.getTitle());
        logo.setSrc(source.getLogoSrc());
        logo.setHref(source.getHomePageUrl());
        sidebar.setLogo(logo);
        sidebar.setMenu(source.getMenu() != null ? p.compile(source.getMenu(), context) : new SimpleMenu());
        sidebar.setExtraMenu(source.getExtraMenu() != null ? p.compile(source.getExtraMenu(), context) : new SimpleMenu());
        sidebar.setSide(source.getSide());
        sidebar.setDefaultState(source.getDefaultState());
        sidebar.setOverlay(source.getOverlay());
        sidebar.setToggledState(source.getToggledState());
        sidebar.setToggleOnHover(source.getToggleOnHover());
        return sidebar;
    }

    private Footer initFooter(N2oFooter source, ApplicationContext context, CompileProcessor p) {
        if (source == null) return null;
        Footer footer = new Footer();
        footer.setSrc(source.getSrc());
        footer.setClassName(source.getCssClass());
        footer.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        footer.setTextRight(source.getRightText());
        footer.setTextLeft(source.getLeftText());
        return footer;
    }

    private void initWelcomePage(N2oApplication source, CompileProcessor p) {
        String welcomePageId;
        if (source.getWelcomePageId() != null)
            welcomePageId = source.getWelcomePageId();
        else
            welcomePageId = p.resolve(property("n2o.homepage.id"), String.class);
        PageContext context = new PageContext(welcomePageId, "/");
        p.addRoute(context);
    }

    @Override
    public Class<N2oApplication> getSourceClass() {
        return N2oApplication.class;
    }
}
