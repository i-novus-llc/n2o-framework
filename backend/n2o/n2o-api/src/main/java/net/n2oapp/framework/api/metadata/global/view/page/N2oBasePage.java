package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.Arrays;
import java.util.List;

/**
 * "Исходная" модель страницы
 */
@Getter
@Setter
public abstract class N2oBasePage extends N2oPage {
    private ActionsBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;

    protected void addWidgets(List<N2oWidget> containers, N2oWidget[] widgets) {
        if (widgets != null && widgets.length != 0)
            containers.addAll(Arrays.asList(widgets));
    }

    protected void addWidgets(List<N2oWidget> containers, N2oAbstractRegion[] regions) {
        if (regions != null)
            for (N2oAbstractRegion region : regions) {
                if (region.getWidgets() != null)
                    containers.addAll(Arrays.asList(region.getWidgets()));
                if (region instanceof N2oRegion)
                    addWidgets(containers, ((N2oRegion) region).getRegions());
            }
    }
}
