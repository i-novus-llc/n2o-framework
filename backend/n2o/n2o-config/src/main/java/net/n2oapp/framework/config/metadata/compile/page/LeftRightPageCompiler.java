package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oLeftRightPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Компиляция страницы с правыми и левыми регионами
 */
@Component
public class LeftRightPageCompiler extends BasePageCompiler<N2oLeftRightPage, StandardPage> {
    @Override
    public StandardPage compile(N2oLeftRightPage source, PageContext context, CompileProcessor p) {
        StandardPage page = new StandardPage();
        List<N2oRegion> allRegions = new ArrayList<>();
        allRegions.addAll(Arrays.asList(source.getLeft()));
        allRegions.addAll(Arrays.asList(source.getRight()));
        if ((source.getLeftWidth() != null && !source.getLeftWidth().isEmpty()) ||
                (source.getRightWidth() != null && !source.getRightWidth().isEmpty()))
            page.setWidth(page.new RegionWidth(source.getLeftWidth(), source.getRightWidth()));
        return compilePage(source, new StandardPage(), context, p, allRegions.toArray(new N2oRegion[0]));
    }

    @Override
    protected void initRegions(N2oLeftRightPage source, StandardPage page, CompileProcessor p,
                               PageContext context, PageScope pageScope) {
        Map<String, List<Region>> regionMap = new HashMap<>();
        IndexScope index = new IndexScope();
        mapRegion(source.getRight(), "right", regionMap, p, context, pageScope, index);
        mapRegion(source.getLeft(), "left", regionMap, p, context, pageScope, index);
        page.setRegions(regionMap);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLeftRightPage.class;
    }

    private void mapRegion(N2oRegion[] regions, String position, Map<String, List<Region>> regionMap,
                           CompileProcessor p, PageContext context, PageScope pageScope, IndexScope index) {
        if (regions != null) {
            List<Region> rightRegion = new ArrayList<>();
            for (N2oRegion n2oRegion : regions) {
                Region region = p.compile(n2oRegion, context, index, pageScope);
                rightRegion.add(region);
            }
            regionMap.put(position, rightRegion);
        }
    }

    @Override
    protected String getPropertyPageSrc() {
        return "n2o.api.page.left-right.src";
    }
}
