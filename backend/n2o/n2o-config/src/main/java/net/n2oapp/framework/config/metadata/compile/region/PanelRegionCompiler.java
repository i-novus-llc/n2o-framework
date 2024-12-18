package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oPanelRegion;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция региона в виде панелей.
 */
@Component
public class PanelRegionCompiler extends BaseRegionCompiler<PanelRegion, N2oPanelRegion> {

    private static final String HEADER_PROPERTY = "n2o.api.region.panel.header";
    private static final String OPEN_PROPERTY = "n2o.api.region.panel.open";
    private static final String COLLAPSIBLE_PROPERTY = "n2o.api.region.panel.collapsible";
    private static final String ROUTABLE_PROPERTY = "n2o.api.region.panel.routable";

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
        region.setHeader(
                castDefault(source.getHeader(), () -> p.resolve(property(HEADER_PROPERTY),  Boolean.class), () -> true)
        );
        region.setFooterTitle(source.getFooterTitle());
        region.setOpen(
                castDefault(source.getOpen(), () -> p.resolve(property(OPEN_PROPERTY),  Boolean.class), () -> true)
        );
        region.setCollapsible(
                castDefault(source.getCollapsible(), () -> p.resolve(property(COLLAPSIBLE_PROPERTY),  Boolean.class), () -> true)
        );
        region.setFullScreen(false);
        compileRoute(source, region.getId(), ROUTABLE_PROPERTY, p);
        region.setHeaderTitle(source.getTitle());
        region.setActiveParam(source.getActiveParam());

        return region;
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("panel", p);
    }
}
