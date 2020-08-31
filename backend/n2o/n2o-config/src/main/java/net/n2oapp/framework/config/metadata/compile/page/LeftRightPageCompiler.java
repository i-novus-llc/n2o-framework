package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oLeftRightPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.PageWidgetsScope;
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
        List<SourceComponent> allItems = new ArrayList<>();
        if (source.getLeft() != null)
            allItems.addAll(Arrays.asList(source.getLeft()));
        if (source.getRight() != null)
            allItems.addAll(Arrays.asList(source.getRight()));
        if ((source.getLeftWidth() != null && !source.getLeftWidth().isEmpty()) ||
                (source.getRightWidth() != null && !source.getRightWidth().isEmpty()))
            page.setWidth(page.new RegionWidth(source.getLeftWidth(), source.getRightWidth()));
        return compilePage(source, page, context, p, allItems.toArray(new SourceComponent[0]), null);
    }

    @Override
    protected void initRegions(N2oLeftRightPage source, StandardPage page, CompileProcessor p, PageContext context,
                               PageScope pageScope, PageRoutes pageRoutes, PageWidgetsScope pageWidgetsScope) {
        Map<String, List<Region>> regionMap = new HashMap<>();
        IndexScope index = new IndexScope();
        mapRegion(source.getRight(), "right", regionMap, p, context, pageScope, index, pageRoutes, pageWidgetsScope);
        mapRegion(source.getLeft(), "left", regionMap, p, context, pageScope, index, pageRoutes, pageWidgetsScope);
        page.setRegions(regionMap);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLeftRightPage.class;
    }

    private void mapRegion(SourceComponent[] items, String position, Map<String, List<Region>> regionMap,
                           CompileProcessor p, PageContext context, Object... scopes) {
        if (items != null) {
            List<Region> regions = new ArrayList<>();
            for (SourceComponent item : items)
                if (item instanceof N2oRegion)
                    regions.add(p.compile(item, context, scopes));
            regionMap.put(position, regions);
        }
    }

    @Override
    protected String getPropertyPageSrc() {
        return "n2o.api.page.left_right.src";
    }
}
