package net.n2oapp.framework.api.metadata.control.properties;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * User: iryabov
 * Date: 07.05.2014
 * Time: 11:33
 */
public enum PopupPlacement implements IdAware {
    DOWN_LEFT("down,left", null),
    DOWN_RIGHT("down,right", "right"),
    UP_LEFT("up,left", "top"),
    UP_RIGHT("up,right", "top right");

    private String xmlName;
    private String cssName;

    PopupPlacement(String xmlName, String cssName) {
        this.xmlName = xmlName;
        this.cssName = cssName;
    }

    public String getCssName() {
        return cssName;
    }

    @Override
    public String getId() {
        return xmlName;
    }
}
