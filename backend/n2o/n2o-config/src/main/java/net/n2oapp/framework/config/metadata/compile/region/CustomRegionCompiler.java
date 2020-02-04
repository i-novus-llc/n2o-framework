package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.CustomRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Компиляция кастомного региона
 */
@Component
public class CustomRegionCompiler extends BaseRegionCompiler<CustomRegion, N2oCustomRegion>{

    @Override
    protected String getPropertyRegionSrc() {
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
        region.setId(p.cast(source.getId(), createId(source, p, "region")));
        region.setPlace(source.getPlace());
        region.setItems(initItems(source, p, Region.Item.class));
        return region;
    }

    @Override
    protected CustomRegion.Item createItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        CustomRegion.Item item = new CustomRegion.Item();
        item.setId("none" + index.get());
        item.setLabel(widget.getName());
        item.setProperties(p.mapAttributes(widget));
        return item;
    }
}
