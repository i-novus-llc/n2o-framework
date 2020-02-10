package net.n2oapp.framework.api.metadata.global.view.fieldset;

/**
 * Исходная модель панели с набором полей.
 */
public class N2oPanelFieldSet extends N2oFieldSet {
    private String icon;
    private Boolean header;
    public static final String DEFAULT_SRC = "PanelFieldset";

    public N2oPanelFieldSet() {
        setSrc(DEFAULT_SRC);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getHeader() {
        return header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }
}
