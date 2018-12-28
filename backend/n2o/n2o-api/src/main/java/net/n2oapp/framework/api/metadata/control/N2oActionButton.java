package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

/**
 * User: operhod
 * Date: 11.08.14
 * Time: 11:51
 */
public class N2oActionButton extends N2oControlActionLink {

    private String icon;
    private LabelType type;
    private String cssClass;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LabelType getType() {
        return type;
    }

    public void setType(LabelType type) {
        this.type = type;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
}
