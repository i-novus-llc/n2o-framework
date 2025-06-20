package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FieldsScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.isInvalidColor;

/**
 * Валидатор филдсета с динамическим числом полей
 */
@Component
public class MultiFieldSetValidator implements SourceValidator<N2oMultiFieldSet>, SourceClassAware {

    @Override
    public void validate(N2oMultiFieldSet source, SourceProcessor p) {
        if (StringUtils.isEmpty(source.getId()))
            throw new N2oMetadataValidationException(String.format("Мультифилдсет виджета %s не имеет идентификатора",
                    ValidationUtils.getIdOrEmptyString(p.getScope(WidgetScope.class).getWidgetId())));
        if (source.getItems() == null)
            throw new N2oMetadataValidationException(String.format("Мультифилдсет %s виджета %s имеет пустое тело",
                    ValidationUtils.getIdOrEmptyString(source.getId()),
                    ValidationUtils.getIdOrEmptyString(p.getScope(WidgetScope.class).getWidgetId())));
        validateItems(source, p);

        if (isInvalidColor(source.getBadgeColor())) {
            throw new N2oMetadataValidationException(
                    String.format("Филдсет <multi-set> использует недопустимое значение атрибута badge-color=\"%s\"",
                            source.getBadgeColor()));
        }
    }

    /**
     * Валидация внутренних элементов
     *
     * @param source Филдсет
     * @param p      Процессор исходных метаданных
     */
    private void validateItems(N2oMultiFieldSet source, SourceProcessor p) {
        FieldsScope scope = new FieldsScope();
        p.safeStreamOf(source.getItems()).forEach(i -> p.validate(i, scope));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiFieldSet.class;
    }
}
