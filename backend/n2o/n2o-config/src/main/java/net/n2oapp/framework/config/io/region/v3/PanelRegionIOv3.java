package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.global.view.region.N2oPanelRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона в виде панелей версии 3.0
 */
@Component
public class PanelRegionIOv3 extends BaseRegionIOv3<N2oPanelRegion> {
    @Override
    public void io(Element e, N2oPanelRegion r, IOProcessor p) {
        super.io(e, r, p);
        p.attribute(e, "title", r::getTitle, r::setTitle);
        p.attributeBoolean(e, "collapsible", r::getCollapsible, r::setCollapsible);
        p.attributeBoolean(e, "header", r::getHeader, r::setHeader);
        p.attribute(e, "icon", r::getIcon, r::setIcon);
        p.attribute(e, "color", r::getColor, r::setColor);
        p.attributeBoolean(e, "open", r::getOpen, r::setOpen);
        p.attribute(e, "footer-title", r::getFooterTitle, r::setFooterTitle);
        p.attribute(e, "active-param", r::getActiveParam, r::setActiveParam);
        p.attributeBoolean(e, "routable", r::getRoutable, r::setRoutable);
    }

    @Override
    public String getElementName() {
        return "panel";
    }

    @Override
    public Class<N2oPanelRegion> getElementClass() {
        return N2oPanelRegion.class;
    }
}
