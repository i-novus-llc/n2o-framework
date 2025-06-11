package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
import org.springframework.stereotype.Component;

/**
 * Связывание региона `<line>` с данными
 */
@Component
public class LineRegionBinder extends BaseRegionBinder<LineRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return LineRegion.class;
    }
}
