package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.NavRegion;
import org.springframework.stereotype.Component;

/**
 * Связывание региона {@code <nav>} с данными
 */
@Component
public class NavRegionBinder extends BaseRegionBinder<NavRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return NavRegion.class;
    }
}
