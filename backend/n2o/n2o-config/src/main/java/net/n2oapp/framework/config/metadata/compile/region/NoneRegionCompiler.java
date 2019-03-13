package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNoneRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.NoneRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
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
    protected String getPropertyRegionSrc() {
        return "n2o.api.region.none.src";
    }

    @Override
    public Class<N2oNoneRegion> getSourceClass() {
        return N2oNoneRegion.class;
    }

    @Override
    public NoneRegion compile(N2oNoneRegion source, PageContext context, CompileProcessor p) {
        NoneRegion region = new NoneRegion();
        build(region, source, context, p);
        region.setPlace(source.getPlace());
        region.setItems(initItems(source, p, Region.Item.class));
        return region;
    }

    @Override
    protected NoneRegion.Item createItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        NoneRegion.Item item = new NoneRegion.Item();
        item.setId("none" + index.get());
        item.setLabel(widget.getName());
        item.setProperties(p.mapAttributes(widget));
        return item;
    }

}
