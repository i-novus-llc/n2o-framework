package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * "Исходная" модель стандартной страницы
 */
@Getter
@Setter
public class N2oStandardPage extends N2oBasePage {
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
