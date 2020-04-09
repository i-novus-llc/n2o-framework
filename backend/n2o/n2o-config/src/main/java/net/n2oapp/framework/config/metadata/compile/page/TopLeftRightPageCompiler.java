package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oTopLeftRightPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.TopLeftRightPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция страницы с тремя регионами
 */
public class TopLeftRightPageCompiler extends BasePageCompiler<N2oTopLeftRightPage, TopLeftRightPage> {

    @Override
    public TopLeftRightPage compile(N2oTopLeftRightPage source, PageContext context, CompileProcessor p) {
        TopLeftRightPage page = new TopLeftRightPage();
        page.setNeedScrollButton(p.cast(source.getScrollTopButton(),
                p.resolve(property("n2o.api.page.top_left_right.need_scroll_button"), Boolean.class)));
        List<N2oRegion> allRegions = new ArrayList<>();

        if (source.getTop() != null || source.getLeft() != null || source.getRight() != null) {
            TopLeftRightPage.Places places = new TopLeftRightPage.Places();

            if (source.getTop() != null) {
                allRegions.addAll(Arrays.asList(source.getTop()));
                places.setTop(compileRegionOptions(source.getTopOptions(), p));
            }
            if (source.getLeft() != null) {
                allRegions.addAll(Arrays.asList(source.getLeft()));
                places.setLeft(compileRegionOptions(source.getLeftOptions(), p));
            }
            if (source.getRight() != null) {
                allRegions.addAll(Arrays.asList(source.getRight()));
                places.setRight(compileRegionOptions(source.getRightOptions(), p));
            }
            page.setPlaces(places);
        }
        return compilePage(source, page, context, p, allRegions.toArray(new N2oRegion[0]), null);
    }

    private TopLeftRightPage.Places.RegionOptions compileRegionOptions(N2oTopLeftRightPage.RegionOptions source, CompileProcessor p) {
        TopLeftRightPage.Places.RegionOptions regionOptions = new TopLeftRightPage.Places.RegionOptions();
        regionOptions.setWidth(source.getWidth());
        regionOptions.setFixed(p.cast(source.getFixed(), p.resolve(property("n2o.api.page.top_left_right.fixed"), Boolean.class)));
        regionOptions.setOffset(source.getOffset());
        return regionOptions;
    }

    @Override
    protected void initRegions(N2oTopLeftRightPage source, TopLeftRightPage page, CompileProcessor p,
                               PageContext context, PageScope pageScope, PageRoutes pageRoutes) {
        Map<String, List<Region>> regionMap = new HashMap<>();
        IndexScope index = new IndexScope();
        mapRegion(source.getTop(), "top", regionMap, p, context, pageScope, index, pageRoutes);
        mapRegion(source.getLeft(), "left", regionMap, p, context, pageScope, index, pageRoutes);
        mapRegion(source.getRight(), "right", regionMap, p, context, pageScope, index, pageRoutes);
        page.setRegions(regionMap);
    }

    private void mapRegion(N2oRegion[] regions, String position, Map<String, List<Region>> regionMap,
                           CompileProcessor p, PageContext context, Object... scopes) {
        if (regions != null) {
            List<Region> regionList = new ArrayList<>();
            for (N2oRegion n2oRegion : regions) {
                Region region = p.compile(n2oRegion, context, scopes);
                regionList.add(region);
            }
            regionMap.put(position, regionList);
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTopLeftRightPage.class;
    }

    @Override
    protected String getPropertyPageSrc() {
        return "n2o.api.page.top_left_right.src";
    }
}
