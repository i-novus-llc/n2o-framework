package net.n2oapp.framework.api.metadata.global.view.page;


import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * "Исходная" модель страницы версии
 */
public class N2oStandardPage extends N2oPage {
    private N2oRegion[] n2oRegions;
    private ActionsBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;
    private String layout;
    private Boolean navigation;

    @Override
    public List<N2oWidget> getContainers() {
        if (n2oRegions == null || n2oRegions.length == 0)
            return null;
        List<N2oWidget> containers = new ArrayList<>();
        for (N2oRegion r : n2oRegions) {
            if (r.getWidgets() != null)
            containers.addAll(Arrays.asList(r.getWidgets()));
        }
        return containers;
    }

//    public static class Toolbar implements Serializable, IdAware {
//        private String place;
//        private String actionId;
//        private String label;
//        private String icon;
//        private String type;
//        private String className;
//
//        @Override
//        public String getId() {
//            return actionId;
//        }
//    }


    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Boolean getNavigation() {
        return navigation;
    }

    public void setNavigation(Boolean navigation) {
        this.navigation = navigation;
    }

    public N2oRegion[] getN2oRegions() {
        return n2oRegions;
    }

    public void setN2oRegions(N2oRegion[] n2oRegions) {
        this.n2oRegions = n2oRegions;
    }

    public ActionsBar[] getActions() {
        return actions;
    }

    public void setActions(ActionsBar[] actions) {
        this.actions = actions;
    }

    public GenerateType getActionGenerate() {
        return actionGenerate;
    }

    public void setActionGenerate(GenerateType actionGenerate) {
        this.actionGenerate = actionGenerate;
    }

    public N2oToolbar[] getToolbars() {
        return toolbars;
    }

    public void setToolbars(N2oToolbar[] toolbars) {
        this.toolbars = toolbars;
    }
}
