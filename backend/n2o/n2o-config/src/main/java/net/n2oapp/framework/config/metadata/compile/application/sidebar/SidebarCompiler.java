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
        sidebar.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.sidebar.src"), String.class)));
        sidebar.setClassName(source.getCssClass());
        sidebar.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        Logo logo = new Logo();
        logo.setTitle(p.resolveJS(source.getTitle()));
        logo.setSrc(source.getLogoSrc());
        logo.setHref(source.getHomePageUrl());
        logo.setClassName(source.getLogoClass());
        sidebar.setLogo(logo);
        sidebar.setPath(source.getPath());
        sidebar.setSubtitle(p.resolveJS(source.getSubtitle()));
        ComponentScope componentScope = new ComponentScope(source);
        sidebar.setMenu(source.getMenu() != null ? p.compile(source.getMenu(), context, componentScope) : new SimpleMenu());
        sidebar.setExtraMenu(source.getExtraMenu() != null ? p.compile(source.getExtraMenu(), context) : new SimpleMenu());
        sidebar.setSide(p.cast(source.getSide(), p.resolve(property("n2o.api.sidebar.side"), Side.class)));
        sidebar.setDefaultState(p.cast(source.getDefaultState(), SidebarState.maxi));
        sidebar.setToggledState(p.cast(source.getToggledState(),
                SidebarState.maxi.equals(sidebar.getDefaultState()) ? SidebarState.mini : SidebarState.maxi, SidebarState.class));
        sidebar.setOverlay(p.cast(source.getOverlay(), p.resolve(property("n2o.api.sidebar.overlay"), Boolean.class)));
        sidebar.setToggleOnHover(p.cast(source.getToggleOnHover(), p.resolve(property("n2o.api.sidebar.toggle_on_hover"), Boolean.class)));
        sidebar.setProperties(p.mapAttributes(source));
        return sidebar;
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
