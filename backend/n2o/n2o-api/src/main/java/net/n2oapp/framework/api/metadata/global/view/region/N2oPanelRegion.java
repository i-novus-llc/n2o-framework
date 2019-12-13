package net.n2oapp.framework.api.metadata.global.view.region;

/**
 * Модель региона в виде панелей
 */
public class N2oPanelRegion extends N2oRegion {
    private String title;
    private Boolean collapsible;
    private Boolean header;


    public Boolean getCollapsible() {
        return collapsible;
    }

    public void setCollapsible(Boolean collapsible) {
        this.collapsible = collapsible;
    }


    public Boolean getHeader() {
        return header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getAlias() {
        return "panel";
    }
}
