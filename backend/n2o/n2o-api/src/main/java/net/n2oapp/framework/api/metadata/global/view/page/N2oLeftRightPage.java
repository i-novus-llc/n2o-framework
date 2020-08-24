package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель страницы с правыми и левыми регионами (left-right-page)
 */
@Getter
@Setter
public class N2oLeftRightPage extends N2oBasePage {
    private N2oAbstractRegion[] left;
    private N2oWidget[] leftRegionWidgets;
    private String leftWidth;
    private N2oAbstractRegion[] right;
    private N2oWidget[] rightRegionWidgets;
    private String rightWidth;

    @Override
    public List<N2oWidget> getContainers() {
        List<N2oWidget> containers = new ArrayList<>();
        addWidgets(containers, leftRegionWidgets);
        addWidgets(containers, left);
        addWidgets(containers, rightRegionWidgets);
        addWidgets(containers, right);
        return containers;
    }
}
