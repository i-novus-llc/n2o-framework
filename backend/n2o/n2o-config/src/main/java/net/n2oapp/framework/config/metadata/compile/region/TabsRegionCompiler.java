package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция региона в виде вкладок
 */
@Component
public class TabsRegionCompiler extends BaseRegionCompiler<TabsRegion, N2oTabsRegion> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.tabs.src";
    }

    @Override
    public Class<N2oTabsRegion> getSourceClass() {
        return N2oTabsRegion.class;
    }

    @Override
    public TabsRegion compile(N2oTabsRegion source, PageContext context, CompileProcessor p) {
        TabsRegion region = new TabsRegion();
        build(region, source, p);
        IndexScope indexScope = p.getScope(IndexScope.class);
        region.setItems(initItems(source, indexScope, context, p));
        region.setAlwaysRefresh(source.getAlwaysRefresh() != null ? source.getAlwaysRefresh() : false);
        region.setLazy(p.cast(source.getLazy(), p.resolve(property("n2o.api.region.tabs.lazy"), Boolean.class)));
        region.setScrollbar(p.cast(source.getScrollbar(), p.resolve(property("n2o.api.region.tabs.scrollbar"), Boolean.class)));
        region.setMaxHeight(p.cast(source.getMaxHeight(), p.resolve(property("n2o.api.region.tabs.max_height"), String.class)));
        region.setHideSingleTab(p.cast(source.getHideSingleTab(),
                p.resolve(property("n2o.api.region.tabs.hide_single_tab"), Boolean.class)));
        compileTabsRoute(source, region.getId(), p);
        return region;
    }

    private void compileTabsRoute(N2oTabsRegion source, String regionId, CompileProcessor p) {
        String activeParam = p.cast(source.getActiveParam(), regionId);
        Boolean routable = p.cast(source.getRoutable(), p.resolve(property("n2o.api.region.tabs.routable"), Boolean.class));

        PageRoutes routes = p.getScope(PageRoutes.class);
        if (routes == null || !Boolean.TRUE.equals(routable))
            return;

        routes.addQueryMapping(
                activeParam,
                Redux.dispatchSetActiveRegionEntity(regionId, activeParam),
                Redux.createActiveRegionEntityLink(regionId)
        );
    }

    @Override
    protected String createId(String regionPlace, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String regionName = pageScope == null ? "tabs" : pageScope.getPageId() + "_tabs";
        return createId(regionPlace, regionName, p);
    }


    protected List<TabsRegion.Tab> initItems(N2oTabsRegion source, IndexScope index,
                                             PageContext context, CompileProcessor p) {
        List<TabsRegion.Tab> items = new ArrayList<>();
        if (source.getTabs() != null)
            for (N2oTabsRegion.Tab t : source.getTabs()) {
                TabsRegion.Tab tab = new TabsRegion.Tab();
                tab.setId(createTabId(t.getId(), source.getAlias(), p));
                tab.setLabel(t.getName());
                tab.setProperties(p.mapAttributes(t));
                tab.setContent(initContent(t.getContent(), context, p, t));
                // opened only first tab
                tab.setOpened(items.isEmpty());
                items.add(tab);
            }
        return items;
    }

    private String createTabId(String regionId, String alias, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String regionName = pageScope == null ? alias : pageScope.getPageId() + "_" + alias;
        String id = p.cast(regionId, createId(null, regionName, p));
        //проверяем id на уникальность
        if (pageScope != null) {
            if (pageScope.getTabIds() == null) {
                pageScope.setTabIds(new HashSet<>());
            } else {
                if (pageScope.getTabIds().contains(id)) {
                    throw new N2oException(String.format("%s tab is already exist", id));
                }
            }
            pageScope.getTabIds().add(id);
        }
        return id;
    }
}
