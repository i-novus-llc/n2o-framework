package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oLineRegion;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.PageWidgetsScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция региона с горизонтальным делителем
 */
@Component
public class LineRegionCompiler extends BaseRegionCompiler<LineRegion, N2oLineRegion> {

    @Override
    protected String getSrcProperty() {
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
        PageWidgetsScope pageWidgetsScope = p.getScope(PageWidgetsScope.class);
        region.setContent(initContent(source.getContent(), indexScope, pageWidgetsScope, context, p));
        region.setLabel(source.getLabel());
        region.setCollapsible(p.cast(source.getCollapsible(),
                p.resolve(property("n2o.api.region.line.collapsible"), Boolean.class)));
        region.setHasSeparator(p.cast(source.getHasSeparator(),
                p.resolve(property("n2o.api.region.line.has_separator"), Boolean.class)));
        region.setExpand(p.cast(source.getExpand(),
                p.resolve(property("n2o.api.region.line.expand"), Boolean.class)));
        return region;
    }

    @Override
    protected String createId(String regionPlace, CompileProcessor p) {
        return createId(regionPlace, "line", p);
    }
}
