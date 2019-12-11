package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

@Component
public class FormValidator implements SourceValidator<N2oForm>, SourceClassAware {

    private IdValidationUtils idValidationUtils;

    public FormValidator(PropertyResolver propertyResolver) {
        idValidationUtils = new IdValidationUtils(propertyResolver);
    }

    @Override
    public void validate(N2oForm source, ValidateProcessor p) {
        idValidationUtils.checkIds(source.getItems());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oForm.class;
    }
}
