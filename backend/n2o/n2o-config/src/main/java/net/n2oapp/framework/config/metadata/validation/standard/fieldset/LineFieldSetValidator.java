package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.isInvalidColor;

/**
 * Валидатор филдсета с горизонтальной линией
 */
@Component
public class LineFieldSetValidator implements SourceValidator<N2oLineFieldSet>, SourceClassAware {

    @Override
    public void validate(N2oLineFieldSet source, SourceProcessor p) {
        p.safeStreamOf(source.getItems()).forEach(p::validate);

        if (isInvalidColor(source.getBadgeColor())) {
            throw new N2oMetadataValidationException(
                    String.format("Филдсет <line> использует недопустимое значение атрибута badge-color=\"%s\"",
                            source.getBadgeColor()));
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLineFieldSet.class;
    }
}
