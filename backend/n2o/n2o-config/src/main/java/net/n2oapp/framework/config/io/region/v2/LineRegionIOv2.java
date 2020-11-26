package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.api.metadata.global.view.region.N2oLineRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона с горизонтальным делителем версии 2.0
 */
@Component
public class LineRegionIOv2 extends BaseRegionIOv2<N2oLineRegion> {
    @Override
    public void io(Element e, N2oLineRegion r, IOProcessor p) {
        super.io(e, r, p);
        p.attribute(e, "label", r::getLabel, r::setLabel);
        p.attributeBoolean(e, "collapsible", r::getCollapsible, r::setCollapsible);
        p.attributeBoolean(e, "has-separator", r::getHasSeparator, r::setHasSeparator);
        p.attributeBoolean(e, "expand", r::getExpand, r::setExpand);
    }

    @Override
    public String getElementName() {
        return "line";
    }

    @Override
    public Class<N2oLineRegion> getElementClass() {
        return N2oLineRegion.class;
    }
}
