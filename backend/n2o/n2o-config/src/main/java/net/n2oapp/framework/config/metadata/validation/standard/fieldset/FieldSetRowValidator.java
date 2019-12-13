package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class FieldSetRowValidator implements SourceValidator<N2oFieldsetRow>, SourceClassAware {

    @Override
    public void validate(N2oFieldsetRow source, ValidateProcessor p) {
        IdValidationUtils.checkIds(source.getItems(), p);
        p.safeStreamOf(source.getItems()).forEach(item -> p.validate(item));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetRow.class;
    }
}
