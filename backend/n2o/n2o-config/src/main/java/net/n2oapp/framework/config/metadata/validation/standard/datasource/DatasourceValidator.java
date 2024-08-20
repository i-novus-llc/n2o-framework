package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор источника данных
 */
public abstract class DatasourceValidator<S extends N2oDatasource> extends AbstractDatasourceValidator<S> {

    @Override
    public void validate(S source, SourceProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getId() == null && widgetScope == null)
            throw new N2oMetadataValidationException(
                    String.format("В одном из источников данных страницы %s не задан 'id'",
                            ValidationUtils.getIdOrEmptyString(pageScope.getPageId())));
        checkDependencies(source, p);
    }

    /**
     * Проверка существования источников данных, указанных в зависимостях
     *
     * @param datasource Источник данных, зависимости которого проверяются
     * @param p          Процессор исходных метаданных
     */
    private void checkDependencies(S datasource, SourceProcessor p) {
        if (datasource.getDependencies() != null) {
            for (N2oDatasource.Dependency d : datasource.getDependencies()) {
                if (d.getOn() == null) {
                    throw new N2oMetadataValidationException(
                            String.format("В зависимости источника данных %s не указан атрибут 'on'",
                                    getIdOrEmptyString(datasource.getId())));
                }
                if (d.getOn() != null) {
                    String on = d.getOn();
                    ValidationUtils.checkDatasourceExistence(on, p,
                            String.format("Атрибут 'on' в зависимости источника данных %s ссылается на несуществующий источник данных '%s'",
                                    getIdOrEmptyString(datasource.getId()), on));
                }
            }
        }
    }
}
