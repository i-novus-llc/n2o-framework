package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор филдсета с горизонтальной линией
 */
@Component
public class LineFieldSetValidator implements SourceValidator<N2oLineFieldSet>, SourceClassAware {

    @Override
    public void validate(N2oLineFieldSet source, SourceProcessor p) {
        IdValidationUtils.checkIds(source.getItems(), p);
        p.safeStreamOf(source.getItems()).forEach(p::validate);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLineFieldSet.class;
    }
}
