package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.CustomRegion;
import org.springframework.stereotype.Component;

/**
 * Связывание региона `<region>` с данными
 */
@Component
public class CustomRegionBinder extends BaseRegionBinder<CustomRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CustomRegion.class;
    }
}
