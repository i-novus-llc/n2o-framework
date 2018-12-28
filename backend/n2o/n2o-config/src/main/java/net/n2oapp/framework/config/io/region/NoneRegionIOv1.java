package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.global.view.region.N2oNoneRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись региона без верстки
 */
@Component
public class NoneRegionIOv1 extends BaseRegionIOv1<N2oNoneRegion> {
    @Override
    public Class<N2oNoneRegion> getElementClass() {
        return N2oNoneRegion.class;
    }

    @Override
    public String getElementName() {
        return "none";
    }

    @Override
    public void io(Element e, N2oNoneRegion m, IOProcessor p) {
        super.io(e, m, p);
    }
}
