package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdInQuotesOrEmptyString;

/**
 * Валидатор исходного источника данных
 */
@Component
public class StandardDatasourceValidator extends AbstractDataSourceValidator<N2oStandardDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardDatasource.class;
    }

    @Override
    public void validate(N2oStandardDatasource datasource, SourceProcessor p) {
        checkForExistsObject(datasource, p);
        N2oQuery query = checkQueryExists(datasource, p);
        DataSourcesScope scope = p.getScope(DataSourcesScope.class);
        checkDependencies(datasource, p);
        checkSubmit(datasource, p);
        checkPrefilters(datasource, query, p);
    }

    /**
     * Проверка существования объекта источника данных
     *
     * @param datasource Источник данных
     * @param p          Процессор исходных метаданных
     */
    private void checkForExistsObject(N2oStandardDatasource datasource, SourceProcessor p) {
        p.checkForExists(datasource.getObjectId(), N2oObject.class,
                String.format("Источник данных '%s' ссылается на несуществующий объект '%s'", datasource.getId(), datasource.getObjectId()));
    }

    /**
     * Проверка существования источников данных, указанных в зависимостях
     *
     * @param datasource Источник данных, зависимости которого проверяются
     * @param p          Процессор исходных метаданных
     */
    private void checkDependencies(N2oStandardDatasource datasource, SourceProcessor p) {
        if (datasource.getDependencies() != null) {
            for (N2oStandardDatasource.Dependency d : datasource.getDependencies()) {
                if (d.getOn() != null) {
                    String on = d.getOn();
                    ValidationUtils.checkDatasourceExistence(on, p,
                            String.format("Атрибут \"on\" в зависимости источника данных '%s' ссылается на несуществующий источник данных '%s'",
                                    datasource.getId(), on));
                }
            }
        }
    }

    /**
     * Проверка существования источников данных, содержащихся в сабмите
     *
     * @param datasource Источник данных, сабмит которого исследуется
     * @param p          Процессор исходных метаданных
     */
    private void checkSubmit(N2oStandardDatasource datasource, SourceProcessor p) {
        if (datasource.getSubmit() != null && datasource.getSubmit().getRefreshDatasourceIds() != null) {
            for (String refreshDs : datasource.getSubmit().getRefreshDatasourceIds()) {
                ValidationUtils.checkDatasourceExistence(refreshDs, p,
                        String.format("Тег <submit> источника данных '%s' содержит несуществующий источник данных '%s' в атрибуте \"refresh-datasources\"",
                                datasource.getId(), refreshDs));
            }
        }
    }

    /**
     * Проверка валидации префильтров источника данных
     *
     * @param datasource Источник данных
     * @param query      Запрос за данными
     * @param p          Процессор исходных метаданных
     */
    private void checkPrefilters(N2oStandardDatasource datasource, N2oQuery query, SourceProcessor p) {
        if (datasource.getFilters() != null) {
            if (query == null)
                throw new N2oMetadataValidationException(
                        String.format("Источник данных %s имеет префильтры, но не задана выборка", getIdInQuotesOrEmptyString(datasource.getId())));
            if (query.getFilters() == null)
                throw new N2oMetadataValidationException(
                        String.format("Источник данных %s имеет префильтры, но в выборке '%s' нет filters!", getIdInQuotesOrEmptyString(datasource.getId()), query.getId()));

            for (N2oPreFilter preFilter : datasource.getFilters()) {
                if (preFilter.getFieldId() == null) {
                    throw new N2oMetadataValidationException(
                            String.format("Источник данных %s содержит префильтр без указанного field-id!", getIdInQuotesOrEmptyString(datasource.getId()))
                    );
                }

                String queryId = ValidationUtils.getIdOrEmptyString(query.getId());
                if (preFilter.getDatasourceId() != null)
                    ValidationUtils.checkDatasourceExistence(preFilter.getDatasourceId(), p,
                            String.format("В префильтре по полю '%s' указан несуществующий источник данных '%s'",
                                    preFilter.getFieldId(), preFilter.getDatasourceId()));
                AbstractField exField = query.getSimpleFieldByAbsoluteId(preFilter.getFieldId());
                if (exField == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке '%s' нет поля '%s'!", queryId, preFilter.getFieldId()));

                if (ArrayUtils.isEmpty(query.getFiltersList(exField.getAbsoluteId())))
                    throw new N2oMetadataValidationException(
                            String.format("В выборке '%s' поле '%s' не содержит фильтров!", queryId, preFilter.getFieldId()));

                N2oQuery.Filter exFilter = null;
                for (N2oQuery.Filter filter : query.getFiltersList(exField.getAbsoluteId())) {
                    if (preFilter.getType() == filter.getType()) {
                        exFilter = filter;
                        break;
                    }
                }
                if (exFilter == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке '%s' поле '%s' не содержит фильтр типа '%s'!",
                                    queryId,
                                    preFilter.getFieldId(),
                                    preFilter.getType()));
            }
        }
    }

    /**
     * Проверка сущестования выборки источника данных
     *
     * @param datasource Источник данных
     * @param p          Процессор исходных метаданных
     * @return Метаданная выборки если она существует, иначе null
     */
    private N2oQuery checkQueryExists(N2oStandardDatasource datasource, SourceProcessor p) {
        if (datasource.getQueryId() != null) {
            p.checkForExists(datasource.getQueryId(), N2oQuery.class,
                    String.format("Источник данных %s ссылается на несуществующую выборку '%s'",
                            getIdInQuotesOrEmptyString(datasource.getId()), datasource.getQueryId()));
            return p.getOrThrow(datasource.getQueryId(), N2oQuery.class);
        }
        return null;
    }

}
