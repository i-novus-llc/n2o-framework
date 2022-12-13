package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Модель региона в виде панелей
 */
@Getter
@Setter
@VisualComponent
public class N2oPanelRegion extends N2oRegion {
    @VisualAttribute
    private String title;
    @VisualAttribute
    private Boolean collapsible;
    @VisualAttribute
    private Boolean header;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private String color;
    @VisualAttribute
    private Boolean open;
    @VisualAttribute
    private String footerTitle;
    private String activeParam;
    private Boolean routable;

    @Override
    public String getAlias() {
        return "panel";
    }
}
