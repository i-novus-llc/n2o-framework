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
    //rename to layout
    private Layout regions;
    //relocate to Layout and rename to src
    private String layout;
    private ActionsBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;

    @Getter
    @Setter
    public static class Layout implements Source, ExtensionAttributesAware {
        private N2oRegion[] regions;
        private Map<N2oNamespace, Map<String, String>> extAttributes;
    }

    @Override
    public List<N2oWidget> getContainers() {
        if (regions == null)
            return Collections.emptyList();
        if (regions.getRegions() == null || regions.getRegions().length == 0)
            return Collections.emptyList();
        List<N2oWidget> containers = new ArrayList<>();
        for (N2oRegion r : regions.getRegions()) {
            if (r.getWidgets() != null)
                containers.addAll(Arrays.asList(r.getWidgets()));
        }
        return containers;
    }

    public N2oRegion[] getN2oRegions() {
        return regions != null ? regions.getRegions() : null;
    }

    public void setN2oRegions(N2oRegion[] n2oRegions) {
        if (this.regions == null)
            this.regions = new Layout();
        this.regions.setRegions(n2oRegions);
    }

    public Map<N2oNamespace, Map<String, String>> getLayoutExtAttributes() {
        return regions != null ? regions.getExtAttributes() : null;
    }

    public void setLayoutExtAttributes(Map<N2oNamespace, Map<String, String>> layoutExtAttributes) {
        if (this.regions == null)
            this.regions = new Layout();
        this.regions.setExtAttributes(layoutExtAttributes);
    }
}
