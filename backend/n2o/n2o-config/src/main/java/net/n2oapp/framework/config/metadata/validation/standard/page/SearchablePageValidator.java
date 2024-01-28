package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import org.apache.commons.lang3.StringUtils;
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
        checkFilters(page);
    }

    /**
     * Проверка источника данных в <search-bar>
     * @param page               Страница с поисковой строкой
     * @param datasourceIdsScope Скоуп источников данных страницы
     */
    private void checkDatasource(N2oSearchablePage page, DatasourceIdsScope datasourceIdsScope) {
        if (page.getSearchBar() != null) {
            if (page.getSearchBar().getDatasourceId() == null)
                throw new N2oMetadataValidationException(
                        String.format("Для страницы '%s' с поисковой строкой необходимо указать источник данных в <search-bar>", page.getId()));
            checkDatasourceLink(page.getSearchBar().getDatasourceId(), datasourceIdsScope,
                    String.format("Элемент <search-bar> страницы '%s' с поисковой строкой ссылается на несуществующий источник данных '%s'", page.getId(), page.getSearchBar().getDatasourceId()));
        }
    }

    private void checkFilters(N2oSearchablePage page) {
        if (StringUtils.isBlank(page.getSearchBar().getSearchFilterId()))
            throw new N2oMetadataValidationException(
                    String.format("Для страницы '%s' с поисковой строкой необходимо указать идентификатор фильтра 'search-filter-id' в <search-bar>",
                            page.getId()));
    }

    /**
     * Проверка ссылки на источник данных
     * @param datasourceId       Идентификатор источника данных
     * @param datasourceIdsScope Скоуп источников данных страницы
     * @param errorMessage       Сообщение об ошибке
     */
    private void checkDatasourceLink(String datasourceId, DatasourceIdsScope datasourceIdsScope, String errorMessage) {
        if (datasourceIdsScope == null || !datasourceIdsScope.contains(datasourceId)) {
            throw new N2oMetadataValidationException(errorMessage);
        }
    }
}
