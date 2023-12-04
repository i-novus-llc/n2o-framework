package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class ConfirmActionValidator implements SourceValidator<N2oConfirmAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConfirmAction.class;
    }

    @Override
    public void validate(N2oConfirmAction source, SourceProcessor p) {
        if (source.getConfirmButtons() != null && source.getConfirmButtons().length > 0) {
            if (source.getConfirmButtons().length == 1)
                if (source.getConfirmButtons()[0] instanceof N2oConfirmAction.OkButton)
                    throw new N2oMetadataValidationException("В действии <confirm> указана кнопка <ok>, но не указана кнопка <cancel>");
                else
                    throw new N2oMetadataValidationException("В действии <confirm> указана кнопка <cancel>, но не указана кнопка <ok>");

            else if (source.getConfirmButtons().length == 2) {
                if (source.getConfirmButtons()[0] instanceof N2oConfirmAction.OkButton && source.getConfirmButtons()[1] instanceof N2oConfirmAction.OkButton) {
                    throw new N2oMetadataValidationException("В действии <confirm> указаны две кнопки <ok>");
                } else if (source.getConfirmButtons()[0] instanceof N2oConfirmAction.CancelButton && source.getConfirmButtons()[1] instanceof N2oConfirmAction.CancelButton) {
                    throw new N2oMetadataValidationException("В действии <confirm> указаны две кнопки <cancel>");
                } else
                    Arrays.stream(source.getConfirmButtons()).forEach(b -> checkColor(b.getColor()));

            } else
                throw new N2oMetadataValidationException("В действии <confirm> указано более двух кнопок");
        }
    }

    /**
     * Проверка использования допустимого значения атрибута цвета
     *
     * @param color значение атрибута
     */
    private static void checkColor(String color) {
        if (color != null && !Objects.equals(color, "link")
                && !color.startsWith("outline")
                && !EnumUtils.isValidEnum(Color.class, color)) {
            throw new N2oMetadataValidationException(
                    String.format("Одна из кнопок действия <confirm> использует недопустимое значение атрибута color=%s",
                            ValidationUtils.getIdOrEmptyString(color))
            );
        }
    }
}
