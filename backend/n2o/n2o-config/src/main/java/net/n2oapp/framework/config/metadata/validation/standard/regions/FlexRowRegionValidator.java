package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.region.N2oFlexRowRegion;
import org.springframework.stereotype.Component;

/**
 * Валидация региона {@code <flex-row>}
 */
@Component
public class FlexRowRegionValidator extends AbstractRegionValidator<N2oFlexRowRegion> {


    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFlexRowRegion.class;
    }
}