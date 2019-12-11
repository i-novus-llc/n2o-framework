package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

@Component
public class FieldSetColumnValidator implements SourceValidator<N2oFieldsetColumn>, SourceClassAware {

    private final IdValidationUtils idValidationUtils;

    public FieldSetColumnValidator(PropertyResolver propertyResolver) {
        idValidationUtils = new IdValidationUtils(propertyResolver);
    }

    @Override
    public void validate(N2oFieldsetColumn source, ValidateProcessor p) {
        idValidationUtils.checkIds(source.getItems());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetColumn.class;
    }
}
