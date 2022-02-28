package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор "Исходной" модели страницы с поисковой строкой
 */
@Component
public class SearchablePageValidator implements SourceValidator<N2oSearchablePage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchablePage.class;
    }

    @Override
    public void validate(N2oSearchablePage page, SourceProcessor p) {
        DatasourceIdsScope datasourceIdsScope = new DatasourceIdsScope();
        p.safeStreamOf(page.getDatasources()).forEach(datasource -> datasourceIdsScope.add(datasource.getId()));
        p.safeStreamOf(page.getWidgets()).filter(widget -> widget.getDatasourceId() == null).forEach(widget -> datasourceIdsScope.add(widget.getId()));
        checkSearchBar(page, datasourceIdsScope);
    }

    /**
     * Проверка метаданной <search-bar>
     * @param page               Страница с поисковой строкой
     * @param datasourceIdsScope Скоуп источников данных страницы
     */
    private void checkSearchBar(N2oSearchablePage page, DatasourceIdsScope datasourceIdsScope) {
        checkDatasource(page, datasourceIdsScope);
    }

    /**
     * Проверка источника данных в <search-bar>
     * @param page               Страница с поисковой строкой
     * @param datasourceIdsScope Скоуп источников данных страницы
     */
    private void checkDatasource(N2oSearchablePage page, DatasourceIdsScope datasourceIdsScope) {
        if (page.getSearchBar() != null) {
            if (page.getSearchBar().getDatasource() == null)
                throw new N2oMetadataValidationException(
                    "Для компиляции страницы с поисковой строкой необходимо указать источник данных в <search-bar>");
            checkDatasourceLink(page.getSearchBar().getDatasource(), datasourceIdsScope,
                    String.format("<search-bar> страницы с поисковой строкой ссылается на несуществующий источник данных %s", page.getSearchBar().getDatasource()));
        }
    }

    /**
     * Проверка ссылки на источник данных
     * @param datasourceId       Идентификатор источника данных
     * @param datasourceIdsScope Скоуп источников данных страницы
     * @param errorMessage       Сообщение об ошибке
     */
    private void checkDatasourceLink(String datasourceId, DatasourceIdsScope datasourceIdsScope, String errorMessage) {
        ValidationUtils.checkForExistsDatasource(datasourceId, datasourceIdsScope, errorMessage);
    }
}
