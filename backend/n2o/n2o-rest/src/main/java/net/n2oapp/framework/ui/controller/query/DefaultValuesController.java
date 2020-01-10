package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

import java.util.Map;

/**
 * Абстрактный контроллер получения данных по умолчанию
 */
public abstract class DefaultValuesController extends GetController {

    protected DefaultValuesController(DataProcessingStack dataProcessingStack,
                                      QueryProcessor queryProcessor,
                                      SubModelsProcessor subModelsProcessor,
                                      MetadataRegister configRegister,
                                      ErrorMessageBuilder errorMessageBuilder) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, configRegister, errorMessageBuilder);
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        try {
            DataSet defaultModel = extractDefaultModel(requestInfo, responseInfo);
            return new GetDataResponse(defaultModel, requestInfo.getCriteria(), responseInfo, requestInfo.getSuccessAlertWidgetId());
        } catch (N2oException e) {
            String widgetId = requestInfo.getFailAlertWidgetId() == null
                    ? requestInfo.getMessagesForm()
                    : requestInfo.getFailAlertWidgetId();
            GetDataResponse response = new GetDataResponse(getErrorMessageBuilder().buildMessages(e), widgetId);
            response.setStatus(e.getHttpStatus());
            return response;
        }

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
            getSubModelsProcessor().executeSubModels(requestInfo.getQuery().getSubModelQueries(), defaultModel);
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
