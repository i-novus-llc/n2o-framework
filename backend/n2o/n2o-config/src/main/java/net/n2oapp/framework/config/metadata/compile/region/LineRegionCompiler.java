package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oLineRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Компиляция региона с горизонтальным делителем.
 */
@Component
public class LineRegionCompiler extends BaseRegionCompiler<LineRegion, N2oLineRegion> {

    @Override
    protected String getPropertyRegionSrc() {
        return "n2o.api.region.line.src";
    }

    @Override
    public Class<N2oLineRegion> getSourceClass() {
        return N2oLineRegion.class;
    }

    @Override
    public LineRegion compile(N2oLineRegion source, PageContext context, CompileProcessor p) {
        LineRegion region = new LineRegion();
        build(region, source, context, p);
        region.setPlace(source.getPlace());
        region.setItems(new ArrayList<>());
        region.setCollapsible(source.getCollapsible());
        region.setItems(initItems(source, context, p, Region.Item.class));
        return region;
    }

    @Override
    protected LineRegion.Item createItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        LineRegion.Item item = new LineRegion.Item();
        item.setId("line" + index.get());
        item.setOpened(p.cast(widget.getOpened(), true));
        item.setLabel(widget.getName());
        item.setProperties(p.mapAttributes(widget));
        return item;
    }


}
