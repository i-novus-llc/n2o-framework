package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.AlignEnum;
import net.n2oapp.framework.api.metadata.global.view.region.JustifyEnum;
import net.n2oapp.framework.api.metadata.global.view.region.N2oFlexRowRegion;
import net.n2oapp.framework.api.metadata.meta.region.FlexRowRegion;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция региона {@code <flex-row>}
 */
@Component
public class FlexRowRegionCompiler extends BaseRegionCompiler<FlexRowRegion, N2oFlexRowRegion> {

    private static final String WRAP_PROPERTY = "n2o.api.region.flex_row.wrap";
    private static final String ALIGN_PROPERTY = "n2o.api.region.flex_row.align";
    private static final String JUSTIFY_PROPERTY = "n2o.api.region.flex_row.justify";

    @Override
    public Class<N2oFlexRowRegion> getSourceClass() {
        return N2oFlexRowRegion.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.flex_row.src";
    }

    @Override
    public FlexRowRegion compile(N2oFlexRowRegion source, PageContext context, CompileProcessor p) {
        FlexRowRegion region = new FlexRowRegion();
        build(region, source, p);
        region.setWrap(castDefault(source.getWrap(),
                () -> p.resolve(property(WRAP_PROPERTY), Boolean.class)));
        region.setAlign(castDefault(source.getAlign(),
                () -> p.resolve(property(ALIGN_PROPERTY), AlignEnum.class)));
        region.setJustify(castDefault(source.getJustify(),
                () -> p.resolve(property(JUSTIFY_PROPERTY), JustifyEnum.class)));
        region.setContent(initContent(source.getContent(), context, p, source));
        return region;
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("flex_row", p);
    }
}