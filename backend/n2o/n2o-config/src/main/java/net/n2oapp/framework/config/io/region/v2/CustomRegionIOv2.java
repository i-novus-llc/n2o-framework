package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кастомного региона версии 2.0
 */
@Component
public class CustomRegionIOv2 extends BaseRegionIOv2<N2oCustomRegion>  {
    @Override
    public Class<N2oCustomRegion> getElementClass() {
        return N2oCustomRegion.class;
    }

    @Override
    public String getElementName() {
        return "region";
    }

    @Override
    public void io(Element e, N2oCustomRegion m, IOProcessor p) {
        super.io(e, m, p);
    }
}
