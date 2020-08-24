package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.List;


/**
 * "Исходная" модель стандартной страницы
 */
@Getter
@Setter
public class N2oStandardPage extends N2oBasePage {
    private N2oAbstractRegion[] regions;
    private N2oWidget[] widgets;

    @Override
    public List<N2oWidget> getContainers() {
        List<N2oWidget> containers = new ArrayList<>();
        addWidgets(containers, widgets);
        addWidgets(containers, regions);
        return containers;
    }
}
