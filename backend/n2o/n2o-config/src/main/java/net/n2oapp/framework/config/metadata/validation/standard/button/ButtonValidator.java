package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор исходной модели кнопки
 */
@Component
public class ButtonValidator implements SourceValidator<N2oButton>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButton.class;
    }

    @Override
    public void validate(N2oButton source, SourceProcessor p) {
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        checkDatasource(source, dataSourcesScope);
        checkValidateDatasource(source, dataSourcesScope);

        if (source.getAction() != null)
            p.validate(source.getAction(), dataSourcesScope);
    }

    /**
     * Проверка существования источника данных для кнопки
     * @param source            Исходная модель кнопки
     * @param dataSourcesScope  Скоуп источников данных
     */
    private void checkDatasource(N2oButton source, DataSourcesScope dataSourcesScope) {
        if (source.getDatasource() != null) {
            String button = ValidationUtils.getIdOrEmptyString(source.getId());
            ValidationUtils.checkForExistsDatasource(source.getDatasource(), dataSourcesScope,
                    String.format("Кнопка %s ссылается на несуществующий источник данных '%s'",
                            button, source.getDatasource()));
        }
    }

    /**
     * Проверка существования валидируемых источников данных
     * @param source            Исходная модель кнопки
     * @param dataSourcesScope  Скоуп источников данных
     */
    private void checkValidateDatasource(N2oButton source, DataSourcesScope dataSourcesScope) {
        if (source.getValidateDatasources() != null) {
            String button = ValidationUtils.getIdOrEmptyString(source.getId());
            for (String validateDs : source.getValidateDatasources()) {
                ValidationUtils.checkForExistsDatasource(validateDs, dataSourcesScope,
                        String.format("Атрибут \"validate-datasources\" кнопки %s содержит несуществующий источник данных '%s'",
                                button, validateDs));
            }
        }
    }
}
