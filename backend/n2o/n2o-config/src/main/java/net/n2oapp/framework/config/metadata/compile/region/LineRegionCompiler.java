package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oLineRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

/**
 * Компиляция региона с горизонтальным делителем
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
        build(region, source, p);
        IndexScope indexScope = p.getScope(IndexScope.class);
        region.setContent(initContent(source.getItems(), indexScope, context, p));
        region.setCollapsible(source.getCollapsible());
        region.setName(source.getName());
        return region;
    }

    @Override
    protected String createId(String regionPlace, CompileProcessor p) {
        return createId(regionPlace, "line", p);
    }

    @Override
    protected Region.Item createWidgetItem(N2oWidget widget, IndexScope index, CompileProcessor p) {
        return null;
    }
}
