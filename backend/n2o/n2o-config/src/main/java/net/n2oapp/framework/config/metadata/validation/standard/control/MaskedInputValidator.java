package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oMaskedInput;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор поля {@code <masked-input>}
 */
@Component
public class MaskedInputValidator implements SourceValidator<N2oMaskedInput>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMaskedInput.class;
    }

    @Override
    public void validate(N2oMaskedInput source, SourceProcessor p) {
        if (source.getMask() == null || source.getMask().isBlank()) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            throw new N2oMetadataValidationException(
                    String.format("В поле %s виджета %s не задан обязательный атрибут 'mask'",
                            ValidationUtils.getIdOrEmptyString(source.getId()),
                            ValidationUtils.getIdOrEmptyString(widgetScope == null ? null : widgetScope.getWidgetId())));
        }
    }
}