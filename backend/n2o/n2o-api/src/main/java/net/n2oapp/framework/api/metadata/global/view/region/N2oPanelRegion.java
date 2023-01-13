package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель региона в виде панелей
 */
@Getter
@Setter
public class N2oPanelRegion extends N2oRegion implements RoutableRegion {
    private String title;
    private Boolean collapsible;
    private Boolean header;
    private String icon;
    private String color;
    private Boolean open;
    private String footerTitle;
    private String activeParam;
    private Boolean routable;

    @Override
    public String getAlias() {
        return "panel";
    }
}
