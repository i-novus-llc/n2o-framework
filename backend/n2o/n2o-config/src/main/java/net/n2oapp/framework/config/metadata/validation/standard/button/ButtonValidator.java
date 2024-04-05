package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.aware.GenerateAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Валидатор исходной модели кнопки
 */
@Component
public class ButtonValidator implements SourceValidator<Button>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return Button.class;
    }

    @Override
    public void validate(Button source, SourceProcessor p) {
        checkDatasource(source, p);
        checkValidateDatasource(source, p);

        if (source.getColor() != null)
            checkColor(source);

        if (source instanceof BadgeAware)
            checkBadgeColor(source);

        if (isNotEmpty(source.getActions())) {
            if (source.getConfirm() != null && (Arrays.stream(source.getActions()).filter(N2oConfirmAction.class::isInstance).count() >= 2))
                throw new N2oMetadataValidationException(String.format("Кнопка %s одновременно имеет атрибут 'confirm' и действие <confirm>", getLabelOrId(source)));
            Arrays.stream(source.getActions()).forEach(a -> p.validate(a, new ComponentScope(source, p.getScope(ComponentScope.class))));
        }

        if (source instanceof GenerateAware && ((GenerateAware) source).getGenerate() != null) {
            String[] generate = ((GenerateAware) source).getGenerate();
            if (generate.length > 1)
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'generate' кнопки %s не может содержать более одного типа генерации", getLabelOrId(source))
                );
            if ((generate.length == 1 && StringUtils.isEmpty(generate[0])))
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'generate' кнопки %s не может содержать пустую строку", getLabelOrId(source))
                );
            if (generate[0].equals("crud"))
                throw new N2oMetadataValidationException(
                        String.format("Атрибут 'generate' кнопки %s не может реализовывать 'crud' генерацию", getLabelOrId(source))
                );
        }
    }

    /**
     * Проверка использования допустимого значения атрибута цвета
     */
    private void checkColor(Button source) {
        String color = source.getColor();
        boolean isIncorrectColor = !Objects.equals(color, "link")
                && !color.startsWith("outline")
                && !EnumUtils.isValidEnum(Color.class, color);
        if (isIncorrectColor && !StringUtils.isLink(color)) {
            throw new N2oMetadataValidationException(
                    String.format("Кнопка %s использует недопустимое значение атрибута color=\"%s\"", getLabelOrId(source), color)
            );
        }
    }

    /**
     * Проверка использования допустимого значения атрибута badge-color
     */
    private void checkBadgeColor(Button source) {
        String badgeColor = ((BadgeAware) source).getBadgeColor();
        if (badgeColor != null && !StringUtils.isLink(badgeColor)
                && !EnumUtils.isValidEnum(Color.class, badgeColor)) {
            throw new N2oMetadataValidationException(
                    String.format("Кнопка %s использует недопустимое значение атрибута badge-color=\"%s\"", getLabelOrId(source), badgeColor)
            );
        }
    }

    /**
     * Проверка существования источника данных для кнопки
     *
     * @param source Исходная модель кнопки
     */
    private void checkDatasource(Button source, SourceProcessor p) {
        if (source.getDatasourceId() != null) {
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    String.format("Кнопка %s ссылается на несуществующий источник данных '%s'",
                            getLabelOrId(source), source.getDatasourceId())
            );
        }
    }

    /**
     * Проверка существования валидируемых источников данных
     *
     * @param source Исходная модель кнопки
     */
    private void checkValidateDatasource(Button source, SourceProcessor p) {
        if (source.getValidateDatasourceIds() != null) {
            for (String validateDs : source.getValidateDatasourceIds()) {
                ValidationUtils.checkDatasourceExistence(validateDs, p,
                        String.format("Атрибут 'validate-datasources' кнопки %s содержит несуществующий источник данных '%s'",
                                getLabelOrId(source), validateDs));
            }
        }
    }

    private String getLabelOrId(Button button) {
        return ValidationUtils.getIdOrEmptyString(button.getLabel() != null ? button.getLabel() : button.getId());
    }
}
