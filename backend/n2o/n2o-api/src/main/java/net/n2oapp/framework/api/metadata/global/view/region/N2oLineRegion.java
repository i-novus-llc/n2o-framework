package net.n2oapp.framework.api.metadata.global.view.region;

/**
 * Модель региона с горизонтальным делителем.
 */
public class N2oLineRegion extends N2oRegion {
    private Boolean collapsible;


    public Boolean getCollapsible() {
        return collapsible;
    }

    public void setCollapsible(Boolean collapsible) {
        this.collapsible = collapsible;
    }


    @Override
    public String getWidgetPrefix() {
        return "line";
    }
}
