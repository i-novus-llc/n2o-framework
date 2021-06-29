package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.BulkOperationUtils;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * User: iryabov
 * Date: 10.05.13
 * Time: 10:58
 */
@Controller
public class BulkActionController extends SetController {

    private AlertMessageBuilder messageBuilder;


    public BulkActionController(DataProcessingStack dataProcessingStack,
                                N2oOperationProcessor actionProcessor,
                                AlertMessageBuilder messageBuilder,
                                MetadataEnvironment environment) {
        super(dataProcessingStack, actionProcessor, environment);
        this.messageBuilder = messageBuilder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SetDataResponse execute(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo) {
        return constructSuccessSetDataResponse(requestInfo, responseInfo);
    }

    private ResponseMessage handleBulkActionRequest(ActionRequestInfo<List<DataSet>> bulkRequestInfo, ActionResponseInfo responseInfo) {
        BulkOperationUtils.Choice choice = BulkOperationUtils.deserializeChoice(bulkRequestInfo.getChoice());
        Map<String, Boolean> ignoreIds = choice.getIgnoreIdsMap();
        for (ActionRequestInfo<DataSet> requestInfo : bulkRequestInfo.toList()) {
            String id = requestInfo.getData().getId();
            if (!ignoreIds.containsKey(id))
                try {
                    handleActionRequest(requestInfo, responseInfo);
                    ignoreIds.put(id, true);
                } catch (Exception e) {
                    ignoreIds.put(id, false);
                    if (choice.isIgnoreAll()) continue;
                    BulkOperationUtils.throwExceptionWithChoice(e, ignoreIds, id, bulkRequestInfo.getOperation());
                }
        }
        return messageBuilder.constructMessage(BulkOperationUtils.createFinalResponse(choice, bulkRequestInfo.getOperation()));
    }


    private SetDataResponse constructSuccessSetDataResponse(ActionRequestInfo<List<DataSet>> requestInfo, ActionResponseInfo responseInfo) {
        SetDataResponse response = new SetDataResponse();
        response.addResponseMessage(handleBulkActionRequest(requestInfo, responseInfo),
                requestInfo.getSuccessAlertWidgetId());
        return response;
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.bulkOperation;
    }
}
