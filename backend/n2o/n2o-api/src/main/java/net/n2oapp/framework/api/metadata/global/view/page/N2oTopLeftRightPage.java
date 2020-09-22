package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Исходная модель страницы с тремя регионами
 */
@Getter
@Setter
public class N2oTopLeftRightPage extends N2oBasePage {
    private Boolean scrollTopButton;
    private N2oRegion[] top;
    private RegionOptions topOptions = new RegionOptions();
    private N2oRegion[] left;
    private RegionOptions leftOptions = new RegionOptions();
    private N2oRegion[] right;
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
        addWidgets(containers, top);
        addWidgets(containers, left);
        addWidgets(containers, right);
        return containers;
    }

    private void addWidgets(List<N2oWidget> containers, N2oRegion[] regions) {
        if (regions != null)
            for (N2oRegion region : regions)
                if (region.getWidgets() != null)
                    containers.addAll(Arrays.asList(region.getWidgets()));
    }
}
