package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static net.n2oapp.framework.config.util.StylesResolver.camelToSnake;

@Component
public class ConfirmActionValidator implements SourceValidator<N2oConfirmAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConfirmAction.class;
    }

    @Override
    public void validate(N2oConfirmAction source, SourceProcessor p) {
        var buttons = source.getConfirmButtons();
        if (buttons == null || buttons.length == 0)
            return;
        switch (buttons.length) {
            case 1 -> validateOneButton(source);
            case 2 -> validateTwoButtons(source);
            default -> throw new N2oMetadataValidationException(
                    "В действии \"<confirm>\" указано более двух кнопок");
        }
    }

    private void validateOneButton(N2oConfirmAction source) {
        if (source.getConfirmButtons()[0] instanceof N2oConfirmAction.OkButton)
            throw new N2oMetadataValidationException(
                    "В действии \"<confirm>\" указана кнопка \"<ok>\", но не указана кнопка \"<cancel>\"");
        throw new N2oMetadataValidationException(
                "В действии \"<confirm>\" указана кнопка \"<cancel>\", но не указана кнопка \"<ok>\"");
    }

    private void validateTwoButtons(N2oConfirmAction source) {
        var buttons = source.getConfirmButtons();
        if (buttons[0] instanceof N2oConfirmAction.OkButton && buttons[1] instanceof N2oConfirmAction.OkButton)
            throw new N2oMetadataValidationException("В действии \"<confirm>\" указаны две кнопки \"<ok>\"");
        if (buttons[0] instanceof N2oConfirmAction.CancelButton && buttons[1] instanceof N2oConfirmAction.CancelButton)
            throw new N2oMetadataValidationException("В действии \"<confirm>\" указаны две кнопки \"<cancel>\"");
        checkColor(buttons[0].getColor());
        checkColor(buttons[1].getColor());
    }

    /**
     * Проверка использования допустимого значения атрибута цвета
     *
     * @param color значение атрибута
     */
    private static void checkColor(String color) {
        if (color != null && !Objects.equals(color, "link")
                && !color.startsWith("outline")
                && !EnumUtils.isValidEnum(ColorEnum.class, camelToSnake(color))) {
            throw new N2oMetadataValidationException(
                    String.format("Одна из кнопок действия \"<confirm>\" использует недопустимое значение атрибута 'color=%s'",
                            ValidationUtils.getIdOrEmptyString(color))
            );
        }
    }
}