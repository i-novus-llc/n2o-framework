package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона в виде вкладок.
 */
@Component
public class TabsRegionIOv1 extends BaseRegionIOv1<N2oTabsRegion> {

    @Override
    public String getElementName() {
        return "tabs";
    }

    @Override
    public Class<N2oTabsRegion> getElementClass() {
        return N2oTabsRegion.class ;
    }
}
