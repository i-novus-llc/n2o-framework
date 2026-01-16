package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.FlexRowRegion;
import org.springframework.stereotype.Component;

/**
 * Связывание региона `<flex-row>` с данными
 */
@Component
public class FlexRowRegionBinder extends BaseRegionBinder<FlexRowRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return FlexRowRegion.class;
    }
}
