package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

@Component
public class FieldSetValidator implements SourceValidator<N2oFieldSet>, SourceClassAware {

    private final IdValidationUtils idValidationUtils;

    public FieldSetValidator(PropertyResolver propertyResolver) {
        idValidationUtils = new IdValidationUtils(propertyResolver);
    }

    @Override
    public void validate(N2oFieldSet source, ValidateProcessor p) {
        idValidationUtils.checkIds(source.getItems());
        p.safeStreamOf(source.getItems()).forEach(item -> p.validate(item));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldSet.class;
    }
}
