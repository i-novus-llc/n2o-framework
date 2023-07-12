package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Валидатор исходной модели кнопки
 */
@Component
public class ButtonValidator implements SourceValidator<N2oButton>, SourceClassAware  {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButton.class;
    }

    @Override
    public void validate(N2oButton source, SourceProcessor p) {
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        checkDatasource(source, datasourceIdsScope);
        if (widgetScope == null)
            checkDatasourceForConfirm(source);
        checkValidateDatasource(source, datasourceIdsScope);

        if (isNotEmpty(source.getActions()))
            Arrays.stream(source.getActions()).forEach(a -> p.validate(a, new ComponentScope(source, p.getScope(ComponentScope.class))));
    }

    /**
     * Проверка существования источника данных для кнопки
     *
     * @param source             Исходная модель кнопки
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkDatasource(N2oButton source, DatasourceIdsScope datasourceIdsScope) {
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
    private void checkValidateDatasource(N2oButton source, DatasourceIdsScope datasourceIdsScope) {
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
     * @param source             Исходная модель кнопки
     */
    private void checkDatasourceForConfirm(N2oButton source) {
        if ((StringUtils.isLink(source.getConfirm()) || StringUtils.isLink(source.getConfirmText())) && source.getDatasourceId() == null)
            throw new N2oMetadataValidationException(String.format("Кнопка %s имеет ссылки в 'confirm' атрибутах, но не ссылается на какой-либо источник данных",
                    ValidationUtils.getIdOrEmptyString(source.getId())));

    }
}
