package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.region.N2oColRegion;
import org.springframework.stereotype.Component;

/**
 * Валидация региона {@code <col>}
 */
@Component
public class ColRegionValidator extends AbstractRegionValidator<N2oColRegion> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oColRegion.class;
    }
}