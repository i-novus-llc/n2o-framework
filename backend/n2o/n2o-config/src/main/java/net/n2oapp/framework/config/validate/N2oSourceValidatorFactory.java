package net.n2oapp.framework.config.validate;

import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.config.factory.FactoryPredicates;

import java.util.List;
import java.util.Map;

public class N2oSourceValidatorFactory extends BaseMetadataFactory<SourceValidator> implements SourceValidatorFactory {

    public N2oSourceValidatorFactory() {
    }

    public N2oSourceValidatorFactory(Map<String, SourceValidator> beans) {
        super(beans);
    }

    @Override
    public <S> void validate(S source, SourceProcessor p) {
        List<SourceValidator> validators = produceList(FactoryPredicates::isSourceAssignableFrom, source);
        for (SourceValidator validator : validators) {
            validator.validate(source, p);
        }
    }

}
