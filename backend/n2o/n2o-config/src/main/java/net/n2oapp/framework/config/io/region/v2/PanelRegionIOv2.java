package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.api.metadata.global.view.region.N2oPanelRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона в виде панелей версии 2.0
 */
@Component
public class PanelRegionIOv2 extends BaseRegionIOv2<N2oPanelRegion> {
    @Override
    public void io(Element e, N2oPanelRegion r, IOProcessor p) {
        super.io(e, r, p);
        // define region title by first widget name or region title if exists
        if (!e.getChildren().isEmpty())
            p.read(e.getChildren().get(0), r, (w, reg) -> reg.setTitle(
                    w.getAttribute("name") != null ? w.getAttribute("name").getValue() : null));
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
