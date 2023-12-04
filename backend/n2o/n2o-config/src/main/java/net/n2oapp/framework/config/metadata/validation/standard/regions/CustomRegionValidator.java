package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import org.springframework.stereotype.Component;

@Component
public class CustomRegionValidator extends AbstractRegionValidator<N2oCustomRegion> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomRegion.class;
    }
}
