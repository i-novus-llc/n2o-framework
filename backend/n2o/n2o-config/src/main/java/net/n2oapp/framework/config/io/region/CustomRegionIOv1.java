package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись кастомного региона
 */
@Component
public class CustomRegionIOv1 extends BaseRegionIOv1<N2oCustomRegion>  {
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
