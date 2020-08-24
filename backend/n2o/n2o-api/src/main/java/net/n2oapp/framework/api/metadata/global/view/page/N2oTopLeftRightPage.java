package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель страницы с тремя регионами
 */
@Getter
@Setter
public class N2oTopLeftRightPage extends N2oBasePage {
    private Boolean scrollTopButton;
    private N2oAbstractRegion[] top;
    private N2oWidget[] topRegionWidgets;
    private RegionOptions topOptions = new RegionOptions();
    private N2oAbstractRegion[] left;
    private N2oWidget[] leftRegionWidgets;
    private RegionOptions leftOptions = new RegionOptions();
    private N2oAbstractRegion[] right;
    private N2oWidget[] rightRegionWidgets;
    private RegionOptions rightOptions = new RegionOptions();

    @Getter
    @Setter
    public static class RegionOptions implements Source {
        private String width;
        private Boolean fixed;
        private Integer offset;
    }

    @Override
    public List<N2oWidget> getContainers() {
        List<N2oWidget> containers = new ArrayList<>();
        addWidgets(containers, topRegionWidgets);
        addWidgets(containers, top);
        addWidgets(containers, leftRegionWidgets);
        addWidgets(containers, left);
        addWidgets(containers, rightRegionWidgets);
        addWidgets(containers, right);
        return containers;
    }
}
