package net.n2oapp.framework.ui.controller.action;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

/**
 * Выполняет действия(action), пришедшие с клиента
 */
@Slf4j
public class OperationController extends SetController {

    private final AlertMessageBuilder messageBuilder;
    private final AlertMessagesConstructor messagesConstructor;
    private final MetadataEnvironment environment;

    public OperationController(DataProcessingStack dataProcessingStack,
                               N2oOperationProcessor operationProcessor,
                               AlertMessageBuilder messageBuilder,
                               MetadataEnvironment environment,
                               AlertMessagesConstructor messagesConstructor) {
        super(dataProcessingStack, operationProcessor);
        this.messageBuilder = messageBuilder;
        this.environment = environment;
        this.messagesConstructor = messagesConstructor;
    }


    @Override
    @SuppressWarnings("unchecked")
    public SetDataResponse execute(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo) {
        return executeRequest(requestInfo, responseInfo);
    }

    protected SetDataResponse executeRequest(ActionRequestInfo<DataSet> requestInfo,
                                             ActionResponseInfo responseInfo) {
        try {
            DataSet data = handleActionRequest(requestInfo, responseInfo);
            SetDataResponse response = constructSuccessSetDataResponse(data, requestInfo, responseInfo);
            responseInfo.setSuccess(true);
            return response;
        } catch (N2oException e) {
            SetDataResponse response = constructFailSetDataResponse(e, requestInfo);
            responseInfo.setSuccess(false);
            log.error(String.format("Error response %d %s: %s", response.getStatus(), e.getSeverity(),
                    e.getUserMessage() != null ? e.getUserMessage() : e.getMessage()), e);
            return response;
        }
    }

    private SetDataResponse constructFailSetDataResponse(N2oException e, ActionRequestInfo<DataSet> requestInfo) {
        SetDataResponse response = new SetDataResponse();
        if (requestInfo.isMessageOnFail()) {
            response.addResponseMessages(messagesConstructor.constructMessages(e, requestInfo), requestInfo.getMessagesForm());
        }
        response.setStatus(e.getHttpStatus());
        return response;
    }

    private SetDataResponse constructSuccessSetDataResponse(DataSet data,
                                                            ActionRequestInfo<DataSet> requestInfo,
                                                            ActionResponseInfo responseInfo) {
        SetDataResponse response = new SetDataResponse();
        response.setResponseMessages(responseInfo.getMessageList(), requestInfo.getMessagesForm());
        response.setData(data);
        if (requestInfo.isMessageOnSuccess())
            response.addResponseMessage(
                    messageBuilder.buildSuccessMessage(requestInfo, data),
                    requestInfo.getMessagesForm());
        return response;
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.operation;
    }

}
