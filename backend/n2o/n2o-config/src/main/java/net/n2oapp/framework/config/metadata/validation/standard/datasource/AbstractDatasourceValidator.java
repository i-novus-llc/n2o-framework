package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
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
        N2oApplication application = p.getScope(N2oApplication.class);

        if (source.getId() == null && widgetScope == null) {
            if (pageScope != null) {
                throw new N2oMetadataValidationException(String.format(
                        "В одном из источников данных страницы %s не задан 'id'",
                        getIdOrEmptyString(pageScope.getPageId())));
            }
            if (application != null) {
                throw new N2oMetadataValidationException(String.format(
                        "В одном из источников данных приложения %s не задан 'id'",
                        getIdOrEmptyString(application.getId())));
            }
        }
    }

    /**
     * Проверка сабмита источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param submit       Сабмит
     * @param objectId     Идентификатор объекта источника данных
     * @param query        Выборка источника данных
     */
    public void checkSubmit(String datasourceId, Submit submit, String objectId, N2oQuery query, SourceProcessor p) {
        if (submit == null)
            return;
        if (submit.getRefreshDatasourceIds() != null) {
            for (String refreshDs : submit.getRefreshDatasourceIds()) {
                ValidationUtils.checkDatasourceExistence(refreshDs, p,
                        String.format("Тег \"<submit>\" источника данных %s содержит несуществующий источник данных '%s' в атрибуте 'refresh-datasources'",
                                getIdOrEmptyString(datasourceId), refreshDs));
            }
        }
        String resolvedObjectId = objectId;
        if (objectId == null && query != null)
            resolvedObjectId = query.getObjectId();
        checkSubmitOperation(datasourceId, submit, resolvedObjectId, p);
    }

    /**
     * Проверка существования операции в объекте, по указанному идентификатору в сабмите
     *
     * @param datasourceId Идентификатор источника данных
     * @param submit       Сабмит
     * @param objectId     Идентификатор объекта, в котором ищется операция
     */
    private void checkSubmitOperation(String datasourceId, Submit submit, String objectId, SourceProcessor p) {
        if (submit.getOperationId() == null)
            throw new N2oMetadataValidationException(
                    String.format("Тег \"<submit>\" источника данных %s не содержит обязательный атрибут 'operation-id'",
                            getIdOrEmptyString(datasourceId)));

        if (objectId == null)
            throw new N2oMetadataValidationException(
                    String.format("Тег \"<submit>\" источника данных %s ссылается на операцию %s, но у источника данных не задан объект",
                            getIdOrEmptyString(datasourceId),
                            getIdOrEmptyString(submit.getOperationId())));
        N2oObject object = p.getOrThrow(objectId, N2oObject.class);

        // существование объекта проверяется отдельно
        if (object == null)
            return;

        boolean exists = p.safeStreamOf(object.getOperations())
                .anyMatch(operation -> submit.getOperationId().equals(operation.getId()));
        if (!exists)
            throw new N2oMetadataValidationException(
                    String.format("Тег \"<submit>\" источника данных %s ссылается на несуществующую в объекте %s операцию %s",
                            getIdOrEmptyString(datasourceId),
                            getIdOrEmptyString(objectId),
                            getIdOrEmptyString(submit.getOperationId())));
    }

    /**
     * Проверка валидации префильтров источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param preFilters   Список фильтров
     * @param query        Запрос за данными
     */
    public void checkPrefilters(String datasourceId, N2oPreFilter[] preFilters, N2oQuery query, SourceProcessor p) {
        if (preFilters == null)
            return;
        checkPrefiltersQuery(datasourceId, query);
        for (N2oPreFilter preFilter : preFilters)
            checkPrefilter(datasourceId, preFilter, query, p);
    }

    private void checkPrefiltersQuery(String datasourceId, N2oQuery query) {
        if (query == null)
            throw new N2oMetadataValidationException(
                    String.format("Источник данных %s имеет префильтры, но не задана выборка",
                            getIdOrEmptyString(datasourceId)));
        if (query.getFilters() == null)
            throw new N2oMetadataValidationException(
                    String.format("Источник данных %s имеет префильтры, но в выборке %s нет \"<filters>\"!",
                            getIdOrEmptyString(datasourceId),
                            getIdOrEmptyString(query.getId())));
    }

    private void checkPrefilter(String datasourceId, N2oPreFilter preFilter, N2oQuery query, SourceProcessor p) {
        if (preFilter.getFieldId() == null)
            throw new N2oMetadataValidationException(
                    String.format("Источник данных %s содержит префильтр без указанного 'field-id'!",
                            getIdOrEmptyString(datasourceId)));

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
                            queryId,
                            getIdOrEmptyString(preFilter.getFieldId())));

        if (ArrayUtils.isEmpty(query.getFiltersList(exField.getAbsoluteId())))
            throw new N2oMetadataValidationException(
                    String.format("В выборке %s поле %s не содержит фильтров!",
                            queryId,
                            getIdOrEmptyString(preFilter.getFieldId())));

        for (N2oQuery.Filter filter : query.getFiltersList(exField.getAbsoluteId())) {
            if (preFilter.getType() == filter.getType())
                return;
        }
        throw new N2oMetadataValidationException(
                String.format("В выборке %s поле %s не содержит фильтр типа %s!",
                        queryId,
                        getIdOrEmptyString(preFilter.getFieldId()),
                        getIdOrEmptyString(preFilter.getType().getId())));
    }
}