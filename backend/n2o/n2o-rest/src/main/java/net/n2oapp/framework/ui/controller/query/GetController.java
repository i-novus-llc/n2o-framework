package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.rest.ControllerTypeAware;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.exception.N2oRecordNotFoundException;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

import java.util.Map;

/**
 * Абстрактный контроллер получения данных
 */
public abstract class GetController implements ControllerTypeAware {

    private DataProcessingStack dataProcessingStack;
    private QueryProcessor queryProcessor;
    private SubModelsProcessor subModelsProcessor;
    private AlertMessageBuilder messageBuilder;


    protected GetController(DataProcessingStack dataProcessingStack,
                            QueryProcessor queryProcessor,
                            SubModelsProcessor subModelsProcessor,
                            AlertMessageBuilder messageBuilder,
                            MetadataEnvironment environment) {
        this.dataProcessingStack = dataProcessingStack;
        this.queryProcessor = queryProcessor;
        this.subModelsProcessor = subModelsProcessor;
        this.messageBuilder = messageBuilder;
    }

    public abstract GetDataResponse execute(QueryRequestInfo requestScope, QueryResponseInfo responseInfo);


    public CollectionPage<DataSet> executeQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        CollectionPage<DataSet> pageData = null;
        dataProcessingStack.processQuery(requestInfo, responseInfo);
        try {
            pageData = queryProcessor.execute(requestInfo.getQuery(), requestInfo.getCriteria());
            executeSubModels(requestInfo, pageData, responseInfo);
        } catch (N2oException e) {
            dataProcessingStack.processQueryError(requestInfo, responseInfo, e);
            throw e;
        } catch (Exception e) {
            throw new N2oException(e, requestInfo.getFailAlertWidgetId());
        }
        dataProcessingStack.processQueryResult(requestInfo, responseInfo, pageData);
        return pageData;
    }

    @SuppressWarnings("unchecked")
    private void executeSubModels(QueryRequestInfo requestInfo, CollectionPage<DataSet> page, QueryResponseInfo responseInfo) {
        if (!page.getCollection().isEmpty() && requestInfo.isSubModelsExists() && requestInfo.getSize() == 1) {
            DataSet dataSet = page.getCollection().iterator().next();
            subModelsProcessor.executeSubModels(requestInfo.getQuery().getSubModelQueries(), dataSet);
        }
    }


    public static class RecordNotFoundCollector implements N2oSubModelsProcessor.OnErrorCallback {
        private QueryResponseInfo responseInfo;

        public RecordNotFoundCollector(QueryResponseInfo responseInfo) {
            this.responseInfo = responseInfo;
        }

        @Override
        public void onError(RuntimeException e, Map<String, Object> dataSet, String controlId) {
            if (e instanceof N2oRecordNotFoundException) {
                dataSet.put(controlId, null);
                ResponseMessage message = new ResponseMessage();
                message.setText("{n2o.fillingErrorValueIsAbsent}");
                message.setSeverityType(SeverityType.danger);
                message.setField(controlId);
                responseInfo.addMessage(message);
            } else {
                ResponseMessage message = new ResponseMessage();
                message.setText("{n2o.fillingErrorDataLoadException}");
                message.setSeverityType(SeverityType.danger);
                message.setField(controlId);
                responseInfo.addMessage(message);
            }
        }
    }

    public DataProcessingStack getDataProcessingStack() {
        return dataProcessingStack;
    }

    public QueryProcessor getQueryProcessor() {
        return queryProcessor;
    }

    public SubModelsProcessor getSubModelsProcessor() {
        return subModelsProcessor;
    }

    public AlertMessageBuilder getMessageBuilder() {
        return messageBuilder;
    }
}
