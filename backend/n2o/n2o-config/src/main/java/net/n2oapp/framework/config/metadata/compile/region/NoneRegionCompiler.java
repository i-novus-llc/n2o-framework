package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNoneRegion;
import net.n2oapp.framework.api.metadata.meta.region.NoneRegion;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.PageWidgetsScope;
import org.springframework.stereotype.Component;

/**
 * Компиляция простого региона
 *
 * @Deprecated replaced by {@link CustomRegionCompiler}
 */
@Deprecated
@Component
public class NoneRegionCompiler extends BaseRegionCompiler<NoneRegion, N2oNoneRegion> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.none.src";
    }

    @Override
    public Class<N2oNoneRegion> getSourceClass() {
        return N2oNoneRegion.class;
    }

    @Override
    public NoneRegion compile(N2oNoneRegion source, PageContext context, CompileProcessor p) {
        NoneRegion region = new NoneRegion();
        build(region, source, p);
        region.setContent(initContent(source.getContent(), context, p, source));
        return region;
    }

    @Override
    protected String createId(String regionPlace, CompileProcessor p) {
        return createId(regionPlace, "none", p);
    }
}
