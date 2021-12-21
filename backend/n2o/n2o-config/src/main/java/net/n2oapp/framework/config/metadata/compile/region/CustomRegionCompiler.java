package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.meta.region.CustomRegion;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Компиляция кастомного региона
 */
@Component
public class CustomRegionCompiler extends BaseRegionCompiler<CustomRegion, N2oCustomRegion>{

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.none.src";
    }

    @Override
    public Class<N2oCustomRegion> getSourceClass() {
        return N2oCustomRegion.class;
    }

    @Override
    public CustomRegion compile(N2oCustomRegion source, PageContext context, CompileProcessor p) {
        CustomRegion region = new CustomRegion();
        build(region, source, p);
        region.setContent(initContent(source.getContent(), context, p, source));
        return region;
    }

    @Override
    protected String createId(String regionPlace, CompileProcessor p) {
        return createId(regionPlace, "region", p);
    }
}
