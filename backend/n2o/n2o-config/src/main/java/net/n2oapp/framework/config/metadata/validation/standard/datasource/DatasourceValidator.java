package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор исходного источника данных
 */
@Component
public class DatasourceValidator implements SourceValidator<N2oDatasource>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatasource.class;
    }

    @Override
    public void validate(N2oDatasource datasource, SourceProcessor p) {
        checkForExistsObject(datasource, p);
        N2oQuery query = checkQueryExists(datasource, p);
        DataSourcesScope scope = p.getScope(DataSourcesScope.class);
        checkDependencies(datasource, scope);
        checkSubmit(datasource, scope);
        checkPrefilters(datasource, query, scope, p);
    }

    /**
     * Проверка существования объекта источника данных
     * @param datasource Источник данных
     * @param p          Процессор исходных метаданных
     */
    private void checkForExistsObject(N2oDatasource datasource, SourceProcessor p) {
        p.checkForExists(datasource.getObjectId(), N2oObject.class,
                String.format("Источник данных '%s' ссылается на несуществующий объект %s", datasource.getId(), datasource.getObjectId()));
    }

    /**
     * Проверка существования источников данных, указанных в зависимостях
     * @param datasource Источник данных, зависимости которого проверяются
     * @param scope      Скоуп источников данных
     */
    private void checkDependencies(N2oDatasource datasource, DataSourcesScope scope) {
        if (datasource.getDependencies() != null) {
            for (N2oDatasource.Dependency d : datasource.getDependencies()) {
                if (d instanceof N2oDatasource.FetchDependency && ((N2oDatasource.FetchDependency) d).getOn() != null) {
                    String on = ((N2oDatasource.FetchDependency) d).getOn();
                    ValidationUtils.checkForExistsDatasource(on, scope,
                            String.format("Атрибут \"on\" в зависимости источника данных '%s' ссылается на несуществующий источник данных '%s'",
                                    datasource.getId(), on));
                }
            }
        }
    }

    /**
     * Проверка существования источников данных, содержащихся в сабмите
     * @param datasource Источник данных, сабмит которого исследуется
     * @param scope      Скоуп источников данных
     */
    private void checkSubmit(N2oDatasource datasource, DataSourcesScope scope) {
        if (datasource.getSubmit() != null && datasource.getSubmit().getRefreshDatasources() != null) {
            for (String refreshDs : datasource.getSubmit().getRefreshDatasources()) {
                ValidationUtils.checkForExistsDatasource(refreshDs, scope,
                        String.format("Тег <submit> источника данных '%s' содержит несуществующий источник данных '%s' в атрибуте \"refresh-datasources\"",
                                datasource.getId(), refreshDs));
            }
        }
    }

    /**
     * Проверка валидации префильтров источника данных
     * @param datasource Источник данных
     * @param query
     * @param scope      Скоуп источников данных
     * @param p          Процессор исходных метаданных
     */
    private void checkPrefilters(N2oDatasource datasource, N2oQuery query, DataSourcesScope scope, SourceProcessor p) {
        if (datasource.getFilters() != null) {
            if (query == null)
                throw new N2oMetadataValidationException(
                        String.format("Источник данных '%s' имеет префильтры, но не задана выборка", datasource.getId()));
            if (query.getFields() == null)
                throw new N2oMetadataValidationException(
                        String.format("Источник данных '%s' имеет префильтры, но в выборке '%s' нет fields!", datasource.getId(), query.getId()));

            for (N2oPreFilter preFilter : datasource.getFilters()) {
                String fieldId = ValidationUtils.getIdOrEmptyString(preFilter.getFieldId());
                String queryId = ValidationUtils.getIdOrEmptyString(query.getId());

                if (preFilter.getDatasource() != null)
                    ValidationUtils.checkForExistsDatasource(preFilter.getDatasource(), scope,
                            String.format("В префильтре по полю %s указан несуществующий источник данных '%s'",
                                    fieldId, preFilter.getDatasource()));

                if (preFilter.getValue() != null && preFilter.getParam() != null && (!Boolean.TRUE.equals(preFilter.getRoutable()))) {
                    throw new N2oMetadataValidationException(
                            String.format("В префильтре по полю %s указан value и param, но при этом routable=false, что противоречит логике работы префильтров!",
                                    fieldId));
                }
                N2oQuery.Field exField = null;
                for (N2oQuery.Field field : query.getFields()) {
                    if (preFilter.getFieldId().equals(field.getId())) {
                        exField = field;
                        break;
                    }
                }
                if (exField == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке %s нет field '%s'!", queryId, preFilter.getFieldId()));

                if (exField.getFilterList() == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке %s field '%s' не содержит фильтров!", queryId, preFilter.getFieldId()));

                N2oQuery.Filter exFilter = null;
                for (N2oQuery.Filter filter : exField.getFilterList()) {
                    if (preFilter.getType() == filter.getType()) {
                        exFilter = filter;
                        break;
                    }
                }
                if (exFilter == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке %s field '%s' не содержит фильтр типа '%s'!",
                                    queryId,
                                    preFilter.getFieldId(),
                                    preFilter.getType()));
            }
        }
    }

    /**
     * Проверка сущестования выборки источника данных
     * @param datasource Источник данных
     * @param p          Процессор исходных метаданных
     * @return           Метаданная выборки если она существует, иначе null
     */
    private N2oQuery checkQueryExists(N2oDatasource datasource, SourceProcessor p) {
        if (datasource.getQueryId() != null) {
            p.checkForExists(datasource.getQueryId(), N2oQuery.class,
                    String.format("Источник данных '%s' ссылается на несуществующую выборку '%s'", datasource.getId(), datasource.getQueryId()));
            return p.getOrThrow(datasource.getQueryId(), N2oQuery.class);
        }
        return null;
    }
}
