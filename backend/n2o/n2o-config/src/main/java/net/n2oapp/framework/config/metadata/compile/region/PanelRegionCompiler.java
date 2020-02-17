package net.n2oapp.framework.config.metadata.compile.region;


import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oPanelRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция региона в виде панелей.
 */
@Component
public class PanelRegionCompiler extends BaseRegionCompiler<PanelRegion, N2oPanelRegion> {

    @Override
    protected String getPropertyRegionSrc() {
        return "n2o.api.region.panel.src";
    }

    @Override
    public Class<N2oPanelRegion> getSourceClass() {
        return N2oPanelRegion.class;
    }

    @Override
    public PanelRegion compile(N2oPanelRegion source, PageContext context, CompileProcessor p) {
        PanelRegion region = new PanelRegion();
        build(region, source, p);
        region.setPlace(source.getPlace());
        region.setClassName(source.getClassName());
        region.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        region.setItems(initItems(source, p, PanelRegion.Panel.class));
        region.setColor(source.getColor());
        region.setIcon(source.getIcon());
        if (region.getItems() != null && !region.getItems().isEmpty()) {
            region.setHasTabs(region.getItems().size() > 1);
        }
        region.setHeader(source.getHeader());
        region.setFooterTitle(source.getFooterTitle());
        region.setOpen(p.cast(source.getOpen(), true));
        region.setCollapsible(source.getCollapsible() != null ? source.getCollapsible() : true);
        region.setFullScreen(false);
        compilePanelRoute(source, region.getId(), p);
        if (source.getTitle() == null && region.getItems().size() == 1) {
            region.setHeaderTitle(region.getItems().get(0).getLabel());
        } else {
            region.setHeaderTitle(source.getTitle());
        }
        return region;
    }

    private void compilePanelRoute(N2oPanelRegion source, String regionId, CompileProcessor p) {
        String activeParam = p.cast(source.getActiveParam(), regionId);
        Boolean routable = p.cast(source.getRoutable(), p.resolve(property("n2o.api.region.panel.routable"), Boolean.class));

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
        return createId(regionPlace, "panel", p);
    }

    @Override
    protected PanelRegion.Panel createItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        PanelRegion.Panel panel = new PanelRegion.Panel();
        panel.setIcon(widget.getIcon());
        panel.setLabel(widget.getName());
        panel.setId("panel" + index.get());
        panel.setOpened(p.cast(widget.getOpened(), true));
        panel.setProperties(p.mapAttributes(widget));
        return panel;
    }
}
