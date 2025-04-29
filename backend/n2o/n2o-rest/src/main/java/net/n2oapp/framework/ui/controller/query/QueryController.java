package net.n2oapp.framework.ui.controller.query;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ControllerTypeEnum;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

/**
 * Контроллер получения выборки данных
 */
@Slf4j
public class QueryController extends GetController {

    private final AlertMessagesConstructor messagesConstructor;

    public QueryController(DataProcessingStack dataProcessingStack,
                           QueryProcessor queryProcessor,
                           SubModelsProcessor subModelsProcessor,
                           AlertMessageBuilder messageBuilder,
                           AlertMessagesConstructor messagesConstructor) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder);
        this.messagesConstructor = messagesConstructor;
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        try {
            CollectionPage<DataSet> collectionPage = executeQuery(requestInfo, responseInfo);
            return new GetDataResponse(collectionPage, responseInfo, requestInfo.getMessagesForm());
        } catch (N2oException e) {
            GetDataResponse response = new GetDataResponse(messagesConstructor.constructMessages(e, requestInfo), requestInfo.getMessagesForm());
            response.setStatus(e.getHttpStatus());
            log.error("Error response {} {}", response.getStatus(), e.getSeverity(), e);
            return response;
        }
    }

    @Override
    public ControllerTypeEnum getControllerType() {
        return ControllerTypeEnum.query;
    }
}
