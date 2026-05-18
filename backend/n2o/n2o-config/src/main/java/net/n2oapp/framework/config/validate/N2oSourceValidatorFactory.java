package net.n2oapp.framework.config.validate;

import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.config.factory.FactoryPredicates;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

public class N2oSourceValidatorFactory extends BaseMetadataFactory<SourceValidator> implements SourceValidatorFactory {

    public N2oSourceValidatorFactory() {
    }

    public N2oSourceValidatorFactory(Map<String, SourceValidator> beans) {
        super(beans);
    }

    @Override
    public <S> void validate(S source, SourceProcessor p) {

        String mode = p.resolve(property("n2o.validation.mode"), String.class);
        if ("off".equals(mode) || "ignore-refs".equals(mode) &&
                source instanceof RefIdAware s && s.getRefId() != null) {
            return;
        }

        List<SourceValidator> validators = produceList(FactoryPredicates::isSourceAssignableFrom, source);
        for (SourceValidator validator : validators) {
            validator.validate(source, p);
        }
    }

}
