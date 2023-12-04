package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;

public abstract class AbstractRegionValidator <R extends N2oRegion> implements SourceValidator<R>, SourceClassAware {

    @Override
    public void validate(R source, SourceProcessor p) {
        SourceComponent[] content = source.getContent();

        p.safeStreamOf(content).forEach(item -> p.validate(item, p));
    }
}
