package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель региона в виде панелей
 */
@Getter
@Setter
public class N2oPanelRegion extends N2oRegion implements RoutableRegion {
    private Boolean header;
    private String title;
    private String footerTitle;
    private Boolean collapsible;
    private Boolean open;
    private String icon;
    private String color;
    private Boolean routable;
    private String activeParam;

    @Override
    public String getAlias() {
        return "panel";
    }
}
