package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор простого филдсета
 */
@Component
public class SetFieldSetValidator implements SourceValidator<N2oSetFieldSet>, SourceClassAware {

    @Override
    public void validate(N2oSetFieldSet source, ValidateProcessor p) {
        IdValidationUtils.checkIds(source.getItems(), p);
        p.safeStreamOf(source.getItems()).forEach(p::validate);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSetFieldSet.class;
    }
}
