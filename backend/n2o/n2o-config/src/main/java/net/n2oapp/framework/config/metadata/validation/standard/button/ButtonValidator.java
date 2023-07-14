package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
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
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        checkDatasource(source, datasourceIdsScope);
        if (widgetScope == null)
            checkDatasourceForConfirm(source);
        checkValidateDatasource(source, datasourceIdsScope);

        checkColor(source.getColor(), "color");
        checkColor(source.getConfirmOkColor(), "confirm-ok-color");
        checkColor(source.getConfirmCancelColor(), "confirm-cancel-color");
        if (source instanceof BadgeAware)
            checkBadgeColor(((BadgeAware) source).getBadgeColor());

        if (isNotEmpty(source.getActions()))
            Arrays.stream(source.getActions()).forEach(a -> p.validate(a, new ComponentScope(source, p.getScope(ComponentScope.class))));
    }

    /**
     * Проверка использования допустимого значения атрибута цвета
     *
     * @param color         значение атрибута
     * @param attributeName имя атрибута
     */
    private static void checkColor(String color, String attributeName) {
        if (color != null && !Objects.equals(color, "link") &&
                !color.startsWith("outline") &&
                !EnumUtils.isValidEnum(Color.class, color)) {
            throw new N2oMetadataValidationException(
                    String.format("Кнопка использует недопустимое значение атрибута %s=\"%s\"", attributeName, color));
        }
    }

    /**
     * Проверка использования допустимого значения атрибута badge-color
     *
     * @param color значение атрибута badge-color
     */
    private static void checkBadgeColor(String color) {
        if (color != null && !StringUtils.isLink(color) &&
                !EnumUtils.isValidEnum(Color.class, color)) {
            throw new N2oMetadataValidationException(
                    String.format("Кнопка использует недопустимое значение атрибута badge-color=\"%s\"", color));
        }
    }

    /**
     * Проверка существования источника данных для кнопки
     *
     * @param source             Исходная модель кнопки
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkDatasource(Button source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getDatasourceId() != null) {
            String button = ValidationUtils.getIdOrEmptyString(source.getId());
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), datasourceIdsScope,
                    String.format("Кнопка %s ссылается на несуществующий источник данных '%s'",
                            button, source.getDatasourceId()));
        }
    }

    /**
     * Проверка существования валидируемых источников данных
     *
     * @param source             Исходная модель кнопки
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkValidateDatasource(Button source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getValidateDatasourceIds() != null) {
            String button = ValidationUtils.getIdOrEmptyString(source.getId());
            for (String validateDs : source.getValidateDatasourceIds()) {
                ValidationUtils.checkDatasourceExistence(validateDs, datasourceIdsScope,
                        String.format("Атрибут 'validate-datasources' кнопки %s содержит несуществующий источник данных '%s'",
                                button, validateDs));
            }
        }
    }

    /**
     * Проверка существования источника данных для кнопки, если
     * confirm или confirm-text являются ссылками
     *
     * @param source Исходная модель кнопки
     */
    private void checkDatasourceForConfirm(Button source) {
        if ((StringUtils.isLink(source.getConfirm()) || StringUtils.isLink(source.getConfirmText())) && source.getDatasourceId() == null)
            throw new N2oMetadataValidationException(String.format("Кнопка %s имеет ссылки в 'confirm' атрибутах, но не ссылается на какой-либо источник данных",
                    ValidationUtils.getIdOrEmptyString(source.getId())));

    }
}
