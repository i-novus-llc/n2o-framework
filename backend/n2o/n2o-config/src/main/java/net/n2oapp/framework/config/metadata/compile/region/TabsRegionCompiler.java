package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Компиляция региона в виде вкладок.
 */
@Component
public class TabsRegionCompiler extends BaseRegionCompiler<TabsRegion, N2oTabsRegion> {
    @Override
    public Class<N2oTabsRegion> getSourceClass() {
        return N2oTabsRegion.class;
    }

    @Override
    public TabsRegion compile(N2oTabsRegion source, PageContext context, CompileProcessor p) {
        TabsRegion region = new TabsRegion();
        build(region, source, context, p);
        region.setSrc("TabsRegion");
        region.setTabs(new ArrayList<>());
        region.setPlace(source.getPlace());
        region.setItems(initItems(source, context, p, TabsRegion.Tab.class));
        return region;
    }

    @Override
    protected TabsRegion.Tab createItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        TabsRegion.Tab tab = new TabsRegion.Tab();
        boolean first = index.isFirst();
        tab.setId("tab" + index.get());
        tab.setLabel(widget.getName());
        tab.setIcon(widget.getIcon());
        tab.setOpened(p.cast(widget.getOpened(), first ? true : null, false));
        tab.setFetchOnInit(tab.getOpened());
        tab.setProperties(p.mapAttributes(widget));
        return tab;
    }

}
