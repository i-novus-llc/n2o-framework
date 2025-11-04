package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oClipboardButton;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидация кнопки {@code <clipboard-button>}
 */
@Component
public class ClipboardButtonValidator implements SourceValidator<N2oClipboardButton>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oClipboardButton.class;
    }

    @Override
    public void validate(N2oClipboardButton source, SourceProcessor p) {
        checkDatasource(source, p);
        checkData(source);
    }

    /**
     * Проверка существования источника данных для кнопки
     */
    private void checkDatasource(N2oClipboardButton source, SourceProcessor p) {
        if (source.getDatasourceId() != null) {
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    String.format("Кнопка копирования %s ссылается на несуществующий источник данных '%s'",
                            getLabelOrId(source), source.getDatasourceId())
            );
        }
    }

    /**
     * Проверка обязательного поля data
     */
    private void checkData(N2oClipboardButton source) {
        if (source.getData() == null) {
            throw new N2oMetadataValidationException(
                    String.format("В кнопке копирования %s не задан обязательный атрибут 'data'", getLabelOrId(source))
            );
        }
    }

    private String getLabelOrId(N2oClipboardButton button) {
        return ValidationUtils.getIdOrEmptyString(button.getLabel() != null ? button.getLabel() : button.getId());
    }
}