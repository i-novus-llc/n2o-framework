package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

public abstract class AbstractDatasourceValidator<S extends N2oAbstractDatasource> implements SourceValidator<S>, SourceClassAware {

    @Override
    public void validate(S source, SourceProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getId() == null && widgetScope == null)
            throw new N2oMetadataValidationException(String.format("В одном из источников данных страницы %s не задан 'id'",
                    getIdOrEmptyString(pageScope.getPageId())));
    }

    /**
     * Проверка существования источников данных, содержащихся в сабмите
     *
     * @param datasourceId Идентификатор источника данных
     * @param submit       Сабмит
     */
    public void checkSubmit(String datasourceId, Submit submit, SourceProcessor p) {
        if (submit != null && submit.getRefreshDatasourceIds() != null) {
            for (String refreshDs : submit.getRefreshDatasourceIds()) {
                ValidationUtils.checkDatasourceExistence(refreshDs, p,
                        String.format("Тег <submit> источника данных %s содержит несуществующий источник данных '%s' в атрибуте 'refresh-datasources'",
                                getIdOrEmptyString(datasourceId), refreshDs));
            }
        }
    }

    /**
     * Проверка валидации префильтров источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param preFilters   Список фильтров
     * @param query        Запрос за данными
     */
    public void checkPrefilters(String datasourceId, N2oPreFilter[] preFilters, N2oQuery query, SourceProcessor p) {
        if (preFilters != null) {
            if (query == null)
                throw new N2oMetadataValidationException(
                        String.format("Источник данных %s имеет префильтры, но не задана выборка",
                                getIdOrEmptyString(datasourceId)));
            if (query.getFilters() == null)
                throw new N2oMetadataValidationException(
                        String.format("Источник данных %s имеет префильтры, но в выборке %s нет filters!",
                                getIdOrEmptyString(datasourceId),
                                getIdOrEmptyString(query.getId())));

            for (N2oPreFilter preFilter : preFilters) {
                if (preFilter.getFieldId() == null) {
                    throw new N2oMetadataValidationException(
                            String.format("Источник данных %s содержит префильтр без указанного field-id!",
                                    getIdOrEmptyString(datasourceId)));
                }

                String queryId = getIdOrEmptyString(query.getId());
                if (preFilter.getDatasourceId() != null)
                    ValidationUtils.checkDatasourceExistence(preFilter.getDatasourceId(), p,
                            String.format("В префильтре по полю %s указан несуществующий источник данных %s",
                                    getIdOrEmptyString(preFilter.getFieldId()),
                                    getIdOrEmptyString(preFilter.getDatasourceId())));
                AbstractField exField = query.getSimpleFieldByAbsoluteId(preFilter.getFieldId());
                if (exField == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке %s нет поля %s!",
                                    getIdOrEmptyString(queryId),
                                    getIdOrEmptyString(preFilter.getFieldId())));

                if (ArrayUtils.isEmpty(query.getFiltersList(exField.getAbsoluteId())))
                    throw new N2oMetadataValidationException(
                            String.format("В выборке %s поле %s не содержит фильтров!",
                                    getIdOrEmptyString(queryId),
                                    getIdOrEmptyString(preFilter.getFieldId())));

                N2oQuery.Filter exFilter = null;
                for (N2oQuery.Filter filter : query.getFiltersList(exField.getAbsoluteId())) {
                    if (preFilter.getType() == filter.getType()) {
                        exFilter = filter;
                        break;
                    }
                }
                if (exFilter == null)
                    throw new N2oMetadataValidationException(
                            String.format("В выборке %s поле %s не содержит фильтр типа %s!",
                                    getIdOrEmptyString(queryId),
                                    getIdOrEmptyString(preFilter.getFieldId()),
                                    getIdOrEmptyString(preFilter.getType().getId())));
            }
        }
    }
}
