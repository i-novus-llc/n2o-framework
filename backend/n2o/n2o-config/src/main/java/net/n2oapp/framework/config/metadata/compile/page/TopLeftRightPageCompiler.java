package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oTopLeftRightPage;
import net.n2oapp.framework.api.metadata.meta.page.TopLeftRightPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция страницы с тремя регионами
 */
@Component
public class TopLeftRightPageCompiler extends BasePageCompiler<N2oTopLeftRightPage, TopLeftRightPage> {

    @Override
    public TopLeftRightPage compile(N2oTopLeftRightPage source, PageContext context, CompileProcessor p) {
        TopLeftRightPage page = new TopLeftRightPage();
        page.setNeedScrollButton(p.cast(source.getScrollTopButton(),
                p.resolve(property("n2o.api.page.top_left_right.need_scroll_button"), Boolean.class)));
        List<SourceComponent> allItems = new ArrayList<>();

        if (source.getTop() != null || source.getLeft() != null || source.getRight() != null) {
            TopLeftRightPage.Places places = new TopLeftRightPage.Places();

            if (source.getTop() != null) {
                allItems.addAll(Arrays.asList(source.getTop()));
                places.setTop(compileRegionOptions(source.getTopOptions(), p));
            }
            if (source.getLeft() != null) {
                allItems.addAll(Arrays.asList(source.getLeft()));
                places.setLeft(compileRegionOptions(source.getLeftOptions(), p));
            }
            if (source.getRight() != null) {
                allItems.addAll(Arrays.asList(source.getRight()));
                places.setRight(compileRegionOptions(source.getRightOptions(), p));
            }
            page.setPlaces(places);
        }

        return compilePage(source, page, context, p, allItems.toArray(new SourceComponent[0]), null);
    }

    private TopLeftRightPage.Places.RegionOptions compileRegionOptions(N2oTopLeftRightPage.RegionOptions source, CompileProcessor p) {
        TopLeftRightPage.Places.RegionOptions regionOptions = new TopLeftRightPage.Places.RegionOptions();
        regionOptions.setWidth(source.getWidth());
        regionOptions.setFixed(p.cast(source.getFixed(), p.resolve(property("n2o.api.page.top_left_right.fixed"), Boolean.class)));
        regionOptions.setOffset(source.getOffset());
        return regionOptions;
    }

    @Override
    protected Map<String, List<Region>> initRegions(N2oTopLeftRightPage source, TopLeftRightPage page,
                                                    CompileProcessor p, PageContext context, Object... scopes) {
        Map<String, List<Region>> regions = new HashMap<>();
        initRegions(source.getTop(), regions, "top", context, p, scopes);
        initRegions(source.getLeft(), regions, "left", context, p,  scopes);
        initRegions(source.getRight(), regions, "right", context, p,  scopes);
        return regions;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTopLeftRightPage.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.top_left_right.src";
    }
}
