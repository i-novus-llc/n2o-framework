package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор простого филдсета
 */
@Component
public class SetFieldSetValidator implements SourceValidator<N2oSetFieldSet>, SourceClassAware {

    @Override
    public void validate(N2oSetFieldSet source, SourceProcessor p) {
        p.safeStreamOf(source.getItems()).forEach(p::validate);

        if (source.getBadgeColor() != null && !StringUtils.isLink(source.getBadgeColor()) &&
                !EnumUtils.isValidEnum(ColorEnum.class, source.getBadgeColor())) {
            throw new N2oMetadataValidationException(
                    String.format("Филдсет <set> использует недопустимое значение атрибута badge-color=\"%s\"",
                            source.getBadgeColor()));
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSetFieldSet.class;
    }
}
