package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

import java.util.Map;

/**
 * Абстрактный контроллер получения данных по умолчанию
 */
public abstract class DefaultValuesController extends GetController {

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        DataSet defaultModel = extractDefaultModel(requestInfo, responseInfo);
        return new GetDataResponse(defaultModel, requestInfo.getCriteria(), requestInfo.getSuccessAlertWidgetId(), responseInfo);
    }

    protected DataSet extractDefaultModel(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        Map<String, Object> fieldsDefaultValues = requestInfo.getQuery().getFieldsDefaultValues();
        DataSet defaultModel = null;
        if (fieldsDefaultValues != null) {
            defaultModel = new DataSet(fieldsDefaultValues);
            defaultModel.merge(requestInfo.getData(), (mainValue, extendValue) -> {
                if (mainValue != null && StringUtils.isDynamicValue(mainValue))
                    return extendValue;
                return mainValue;
            });
        } else {
            defaultModel = new DataSet();
        }

        if (requestInfo.getQuery() != null) {
            subModelsProcessor.executeSubModels(requestInfo.getQuery().getSubModelQueries(), defaultModel);
            CollectionPage<DataSet> queryDefaultPage;
            queryDefaultPage = executeQuery(requestInfo, responseInfo);
            if (!queryDefaultPage.getCollection().isEmpty()) {
                DataSet queryDefaultModel = queryDefaultPage.getCollection().iterator().next();
                defaultModel.merge(queryDefaultModel, DataSet.EXTEND_IF_VALUE_NOT_NULL);
            }
        }

        return defaultModel;
    }

}
