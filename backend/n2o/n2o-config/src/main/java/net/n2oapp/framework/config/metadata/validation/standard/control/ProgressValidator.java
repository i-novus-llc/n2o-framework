package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oProgress;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.isInvalidColor;

@Component
public class ProgressValidator implements SourceValidator<N2oProgress>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oProgress.class;
    }

    @Override
    public void validate(N2oProgress source, SourceProcessor p) {
        if (isInvalidColor(source.getColor())) {
            throw new N2oMetadataValidationException(
                    String.format("В поле <progress> указано недопустимое значение атрибута color=\"%s\"",
                            source.getColor()));
        }
    }
}