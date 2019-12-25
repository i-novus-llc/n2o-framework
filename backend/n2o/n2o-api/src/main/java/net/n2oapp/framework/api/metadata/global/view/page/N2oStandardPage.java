package net.n2oapp.framework.api.metadata.global.view.page;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.*;


/**
 * "Исходная" модель страницы версии
 */
@Getter
@Setter
public class N2oStandardPage extends N2oPage {
    private ActionsBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;
    private N2oRegion[] regions;

    @Override
    public List<N2oWidget> getContainers() {
        if (regions == null || regions.length == 0)
            return Collections.emptyList();
        List<N2oWidget> containers = new ArrayList<>();
        for (N2oRegion r : regions) {
            if (r.getWidgets() != null)
                containers.addAll(Arrays.asList(r.getWidgets()));
        }
        return containers;
    }

}
