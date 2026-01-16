package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.RowRegion;
import org.springframework.stereotype.Component;

@Component
public class RowRegionBinder  extends BaseRegionBinder<RowRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return RowRegion.class;
    }
}
