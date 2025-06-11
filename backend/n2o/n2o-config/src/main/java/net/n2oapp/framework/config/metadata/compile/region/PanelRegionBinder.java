package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import org.springframework.stereotype.Component;

/**
 * Связывание региона `<panel>` с данными
 */
@Component
public class PanelRegionBinder extends BaseRegionBinder<PanelRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return PanelRegion.class;
    }
}
