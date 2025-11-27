package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oColRegion;
import net.n2oapp.framework.api.metadata.meta.region.ColRegion;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Компиляция региона {@code <col>}
 */
@Component
public class ColRegionCompiler extends BaseRegionCompiler<ColRegion, N2oColRegion> {

    @Override
    public Class<N2oColRegion> getSourceClass() {
        return N2oColRegion.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.col.src";
    }

    @Override
    public ColRegion compile(N2oColRegion source, PageContext context, CompileProcessor p) {
        ColRegion region = new ColRegion();
        build(region, source, p);
        region.setSize(source.getSize());
        region.setOffset(source.getOffset());
        region.setContent(initContent(source.getContent(), context, p, source));
        return region;
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("col", p);
    }
}
