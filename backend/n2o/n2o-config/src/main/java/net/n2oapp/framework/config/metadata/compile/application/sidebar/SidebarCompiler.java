package net.n2oapp.framework.config.metadata.compile.application.sidebar;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.*;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция боковой панели
 */
@Component
public class SidebarCompiler implements BaseSourceCompiler<Sidebar, N2oSidebar, ApplicationContext>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSidebar.class;
    }

    @Override
    public Sidebar compile(N2oSidebar source, ApplicationContext context, CompileProcessor p) {
        Sidebar sidebar = new Sidebar();
        initDatasource(sidebar, source, p);
        sidebar.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.sidebar.src"), String.class)));
        sidebar.setClassName(source.getCssClass());
        sidebar.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        sidebar.setProperties(p.mapAttributes(source));

        sidebar.setLogo(initLogo(source, p));
        sidebar.setPath(source.getPath());
        sidebar.setSubtitle(p.resolveJS(source.getSubtitle()));
        ComponentScope componentScope = new ComponentScope(source);
        sidebar.setMenu(source.getNavMenu() != null
                ? p.compile(source.getNavMenu(), context, componentScope)
                : new SimpleMenu());
        sidebar.setExtraMenu(source.getExtraMenu() != null
                ? p.compile(source.getExtraMenu(), context, componentScope)
                : new SimpleMenu());
        sidebar.setSide(castDefault(source.getSide(),
                () -> p.resolve(property("n2o.api.sidebar.side"), SidebarSideEnum.class)));
        sidebar.setDefaultState(castDefault(source.getDefaultState(), SidebarStateEnum.MAXI));
        sidebar.setToggledState(castDefault(source.getToggledState(),
                SidebarStateEnum.MAXI.equals(sidebar.getDefaultState())
                        ? SidebarStateEnum.MINI
                        : SidebarStateEnum.MAXI));
        sidebar.setOverlay(castDefault(source.getOverlay(),
                () -> p.resolve(property("n2o.api.sidebar.overlay"), Boolean.class)));
        sidebar.setToggleOnHover(castDefault(source.getToggleOnHover(),
                () -> p.resolve(property("n2o.api.sidebar.toggle_on_hover"), Boolean.class)));
        return sidebar;
    }

    private Logo initLogo(N2oSidebar source, CompileProcessor p) {
        Logo logo = new Logo();
        logo.setTitle(p.resolveJS(source.getTitle()));
        logo.setSrc(source.getLogoSrc());
        logo.setHref(source.getHomePageUrl());
        logo.setClassName(source.getLogoClass());
        return logo;
    }

    private void initDatasource(Sidebar compiled, N2oSidebar source, CompileProcessor p) {
        if (source.getDatasourceId() == null && source.getDatasource() == null)
            return;

        String datasourceId = source.getDatasourceId();
        N2oAbstractDatasource datasource;
        if (source.getDatasourceId() == null) {
            datasource = source.getDatasource();
            datasourceId = datasource.getId();
            source.setDatasourceId(datasourceId);
            source.setDatasource(null);
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            if (dataSourcesScope != null)
                dataSourcesScope.put(datasourceId, datasource);
        } else {
            DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
            datasource = dataSourcesScope.get(datasourceId);
        }
        datasource.setSize(p.resolve(property("n2o.api.sidebar.size"), Integer.class));
        compiled.setDatasource(datasourceId);
    }
}
