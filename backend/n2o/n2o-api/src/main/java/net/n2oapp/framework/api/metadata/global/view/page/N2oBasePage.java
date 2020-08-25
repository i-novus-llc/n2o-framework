package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
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

    protected void addWidgets(List<N2oWidget> containers, SourceComponent[] items) {
        if (items != null && items.length != 0)
            for (SourceComponent item : items)
                if (item instanceof N2oWidget)
                    containers.add((N2oWidget) item);
                else if (item instanceof N2oRegion)
                    addWidgets(containers, ((N2oRegion) item).getItems());
    }
}
