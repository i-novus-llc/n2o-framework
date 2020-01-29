package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class N2OLeftRightPage extends N2OBasePage {

    private N2oRegion[] left;
    private String leftWidth;
    private N2oRegion[] right;
    private String rightWidth;

    @Override
    public List<N2oWidget> getContainers() {
        List<N2oWidget> containers = new ArrayList<>();
        if (left != null && left.length > 0) {
            for (N2oRegion r : left) {
                if (r.getWidgets() != null)
                    containers.addAll(Arrays.asList(r.getWidgets()));
            }
        }
        if (right != null && right.length > 0) {
            for (N2oRegion r : right) {
                if (r.getWidgets() != null)
                    containers.addAll(Arrays.asList(r.getWidgets()));
            }
        }
        if (containers.isEmpty())
            return Collections.emptyList();
        return containers;
    }
}
