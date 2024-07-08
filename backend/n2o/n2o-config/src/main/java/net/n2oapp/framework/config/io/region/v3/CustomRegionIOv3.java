package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кастомного региона версии 3.0
 */
@Component
public class CustomRegionIOv3 extends BaseRegionIOv3<N2oCustomRegion> {
    @Override
    public Class<N2oCustomRegion> getElementClass() {
        return N2oCustomRegion.class;
    }

    @Override
    public String getElementName() {
        return "region";
    }
}
