package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oFlexRowRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil.collectWidgets;

/**
 * @deprecated
 * Исходная модель страницы с тремя регионами
 */
@Deprecated(since = "7.29")
@Getter
@Setter
public class N2oTopLeftRightPage extends N2oBasePage {
    private Boolean scrollTopButton;
    private SourceComponent[] top;
    private RegionOptions topOptions = new RegionOptions();
    private SourceComponent[] left;
    private RegionOptions leftOptions = new RegionOptions();
    private SourceComponent[] right;
    private RegionOptions rightOptions = new RegionOptions();

    @Getter
    @Setter
    public static class RegionOptions implements Source {
        private String width;
        private Boolean fixed;
        private Integer offset;
    }

    @Override
    public List<N2oWidget> getWidgets() {
        List<SourceComponent> sourceComponents = new ArrayList<>();

        if (top != null)
            sourceComponents.addAll(Arrays.asList(top));
        if (left != null)
            sourceComponents.addAll(Arrays.asList(left));
        if (right != null)
            sourceComponents.addAll(Arrays.asList(right));

        return collectWidgets(sourceComponents.toArray(new SourceComponent[0]));
    }

    /**
     * Преобразование <top> в <flex-row>; <left> и <right> в <flex-row>
     * Если ширина задана, то обернуть содержимое в <region>, которому задать стили.
     */
    public void adapterV4() {
        if (top == null && left == null && right == null)
            return;
        // top
        List<SourceComponent> regionContent = new ArrayList<>();
        addContent(regionContent, top, topOptions.width);

        N2oFlexRowRegion topFlexRowRegion = new N2oFlexRowRegion();
        topFlexRowRegion.setContent(regionContent.toArray(new SourceComponent[0]));

        // left и right
        String[] calculatedWidths = calculateWidths(leftOptions.width, rightOptions.width);

        regionContent.clear();
        addContent(regionContent, left, calculatedWidths[0]);
        addContent(regionContent, right, calculatedWidths[1]);

        N2oFlexRowRegion lrFlexRowRegion = new N2oFlexRowRegion();
        lrFlexRowRegion.setContent(regionContent.toArray(new SourceComponent[0]));

        // page
        setItems(new SourceComponent[]{topFlexRowRegion, lrFlexRowRegion});
    }
}