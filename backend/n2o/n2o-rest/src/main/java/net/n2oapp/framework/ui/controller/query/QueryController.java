package net.n2oapp.framework.ui.controller.query;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * Контроллер получения выборки данных
 */
@Controller
public class QueryController extends GetController {

    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    public QueryController(DataProcessingStack dataProcessingStack,
                           QueryProcessor queryProcessor,
                           SubModelsProcessor subModelsProcessor,
                           AlertMessageBuilder messageBuilder,
                           MetadataEnvironment environment) {
        super(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder, environment);
    }

    @Override
    public GetDataResponse execute(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        try {
            CollectionPage<DataSet> collectionPage = executeQuery(requestInfo, responseInfo);
            return new GetDataResponse(collectionPage, responseInfo, requestInfo.getMessagesForm());
        } catch (N2oException e) {
            GetDataResponse response = new GetDataResponse(getMessageBuilder().buildMessages(e, requestInfo), requestInfo.getMessagesForm());
            response.setStatus(e.getHttpStatus());
            logger.error("Error response " + response.getStatus() + " " + e.getSeverity(), e);
            return response;
        }
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.query;
    }
}
