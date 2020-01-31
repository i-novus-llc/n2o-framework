package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция региона в виде вкладок.
 */
@Component
public class TabsRegionCompiler extends BaseRegionCompiler<TabsRegion, N2oTabsRegion> {

    @Override
    protected String getPropertyRegionSrc() {
        return "n2o.api.region.tabs.src";
    }

    @Override
    public Class<N2oTabsRegion> getSourceClass() {
        return N2oTabsRegion.class;
    }

    @Override
    public TabsRegion compile(N2oTabsRegion source, PageContext context, CompileProcessor p) {
        TabsRegion region = new TabsRegion();
        build(region, source, context, p);
        if (source.getId() == null)
            region.setId(createId(source, region.getId()));
        region.setTabs(new ArrayList<>());
        region.setPlace(source.getPlace());
        region.setItems(initItems(source, p, TabsRegion.Tab.class));
        region.setAlwaysRefresh(source.getAlwaysRefresh() != null ? source.getAlwaysRefresh() : false);
        region.setLazy(p.cast(source.getLazy(), p.resolve(property("n2o.api.default.region.tabs.lazy"), Boolean.class)));
        region.setActiveParam(p.cast(source.getActiveParam(), region.getId()));
        region.setRoutable(p.cast(source.getRoutable(), p.resolve(property("n2o.api.default.region.tabs.routable"), Boolean.class)));
        compileTabsRoute(region, p);
        return region;
    }

    private String createId(N2oTabsRegion source, String oldId) {
        StringJoiner id = new StringJoiner("_");
        if (source.getPlace() != null)
            id.add(source.getPlace());
        id.add("tab");

        int idx = oldId.length();
        while (Character.isDigit(oldId.charAt(--idx))) ;

        id.add(oldId.substring(idx + 1));
        return id.toString();
    }

    private void compileTabsRoute(TabsRegion region, CompileProcessor p) {
        PageRoutes routes = p.getScope(PageRoutes.class);
        if (routes == null || !Boolean.TRUE.equals(region.getRoutable()))
            return;

        ReduxAction onGet = new ReduxAction("n2o/regions/SET_ACTIVE_REGION_ENTITY",
                new HashMap<String, Object>() {{
                    put("regionId", region.getId());
                    put("activeEntity", ":" + region.getActiveParam());
        }});
        BindLink onSet = new BindLink("regions." + region.getId() + ".activeEntity");
        routes.addQueryMapping(region.getActiveParam(), onGet, onSet);
    }

    @Override
    protected TabsRegion.Tab createItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        TabsRegion.Tab tab = new TabsRegion.Tab();
        boolean first = index.isFirst();
        tab.setId("tab" + index.get());

        String label = null;
        String icon = null;
        if (widget.getRefId() != null) {
            N2oWidget referable = p.getSource(widget.getRefId(), widget.getClass());
            if (referable != null) {
                label = referable.getName();
                icon = referable.getIcon();
            }
        }
        tab.setLabel(p.cast(widget.getName(), label));
        tab.setIcon(p.cast(widget.getIcon(), icon));

        tab.setOpened(p.cast(widget.getOpened(), first ? true : null, false));
        tab.setFetchOnInit(tab.getOpened());
        tab.setProperties(p.mapAttributes(widget));
        return tab;
    }

}
