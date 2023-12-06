package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.region.N2oLineRegion;
import org.springframework.stereotype.Component;

@Component
public class LineRegionValidator extends AbstractRegionValidator<N2oLineRegion> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLineRegion.class;
    }
}
