package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.ColRegion;
import org.springframework.stereotype.Component;

/**
 * Связывание региона `<col>` с данными
 */
@Component
public class ColRegionBinder extends BaseRegionBinder<ColRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ColRegion.class;
    }
}
