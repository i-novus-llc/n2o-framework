package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.control.N2oStatus;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.util.StylesResolver.camelToSnake;

@Component
public class StatusValidator implements SourceValidator<N2oStatus>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStatus.class;
    }

    @Override
    public void validate(N2oStatus source, SourceProcessor p) {
        if (source.getColor() != null &&
                !EnumUtils.isValidEnum(ColorEnum.class, camelToSnake(source.getColor()))) {
            throw new N2oMetadataValidationException(
                    String.format("В поле <status> указано недопустимое значение атрибута color=\"%s\"",
                            source.getColor()));
        }
    }
}