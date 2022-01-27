package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор виджета форма
 */
@Component
public class FormValidator implements SourceValidator<N2oForm>, SourceClassAware {

    @Override
    public void validate(N2oForm source, SourceProcessor p) {
        ValidationUtils.checkIds(source.getItems(), p);
        FieldsScope scope = new FieldsScope();
        checkWhiteListValidation(source, p);
        p.safeStreamOf(source.getItems()).forEach(item -> p.validate(item, scope));
        p.safeStreamOf(source.getActions()).forEach(actionsBar -> p.validate(actionsBar.getAction()));
    }

    /**
     * Проверка наличия в форме ссылки на корректный источник данных при наличии white-list валидации на ее полях
     * @param source Форма
     * @param p      Процессор исходных метаданных
     */
    private void checkWhiteListValidation(N2oForm source, SourceProcessor p) {
        if (source.getItems() != null) {
            for (SourceComponent item : source.getItems()) {
                if (item instanceof N2oField) {
                    N2oField.Validations validations = ((N2oField) item).getValidations();
                    if (validations != null && validations.getWhiteList() != null) {
                        if (source.getDatasource() != null) {
                            checkInlineDatasource(source);
                            break;
                        }
                        checkDatasource(source, p);
                    }
                }
            }
        }
    }

    /**
     * Проверка внутреннего источника данных формы
     * @param source Форма
     */
    private void checkInlineDatasource(N2oForm source) {
        checkDatasourceObject(source, source.getDatasource());
    }

    /**
     * Проверка наличия ссылки на источник данных у формы
     * @param source Форма
     * @param p      Процессор исходных метаданных
     */
    private void checkDatasource(N2oForm source, SourceProcessor p) {
        if (source.getDatasourceId() == null)
            throw new N2oMetadataValidationException(
                    String.format("Для компиляции формы %s необходимо указать атрибут datasource или ввести внутренний источник данных",
                            source.getId())
            );
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        if (dataSourcesScope != null)
            checkDatasourceObject(source, dataSourcesScope.get(source.getDatasourceId()));
    }

    /**
     * Проверка наличия атрибута object у указанного в форме источника данных
     * @param source     Форма
     * @param datasource Источник данных
     */
    private void checkDatasourceObject(N2oForm source, N2oDatasource datasource) {
        if (datasource.getObjectId() == null)
            throw new N2oMetadataValidationException(
                    String.format("Для компиляции формы %s необходимо указать объект источника данных %s",
                            source.getId(), ValidationUtils.getIdOrEmptyString(source.getDatasourceId()))
            );
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oForm.class;
    }
}