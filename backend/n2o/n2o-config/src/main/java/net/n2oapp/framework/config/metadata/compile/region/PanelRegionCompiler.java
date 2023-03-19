package net.n2oapp.framework.config.metadata.compile.region;


import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oPanelRegion;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция региона в виде панелей.
 */
@Component
public class PanelRegionCompiler extends BaseRegionCompiler<PanelRegion, N2oPanelRegion> {

    @Override
    protected String getSrcProperty() {
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
        region.setContent(initContent(source.getContent(), context, p, source));
        region.setColor(source.getColor());
        region.setIcon(source.getIcon());
        region.setHeader(p.cast(source.getHeader(),
                p.resolve(property("n2o.api.region.panel.header"),  Boolean.class), true));
        region.setFooterTitle(source.getFooterTitle());
        region.setOpen(p.cast(source.getOpen(),
                p.resolve(property("n2o.api.region.panel.open"),  Boolean.class), true));
        region.setCollapsible(p.cast(source.getCollapsible(),
                p.resolve(property("n2o.api.region.panel.collapsible"),  Boolean.class), true));
        region.setFullScreen(false);
        compileRoute(source, region.getId(), "n2o.api.region.panel.routable", p);
        region.setHeaderTitle(source.getTitle());
        return region;
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("panel", p);
    }
}
