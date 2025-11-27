package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.global.view.region.N2oColRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись региона {@code <col>} версии 3.0
 */
@Component
public class ColRegionIOv3 extends BaseRegionIOv3<N2oColRegion> {

    @Override
    public void io(Element e, N2oColRegion r, IOProcessor p) {
        super.io(e, r, p);
        p.attributeInteger(e, "size", r::getSize, r::setSize);
        p.attributeInteger(e, "offset", r::getOffset, r::setOffset);
    }

    @Override
    public Class<N2oColRegion> getElementClass() {
        return N2oColRegion.class;
    }

    @Override
    public String getElementName() {
        return "col";
    }
}