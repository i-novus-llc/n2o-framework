package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * Вылняет действие(action) пришедшие с клиента
 */
@Controller
public class OperationController extends SetController {

    private ErrorMessageBuilder errorMessageBuilder;
    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);

    public OperationController(DataProcessingStack dataProcessingStack,
                               DomainProcessor domainsProcessor,
                               N2oOperationProcessor operationProcessor,
                               ErrorMessageBuilder errorMessageBuilder) {
        super(dataProcessingStack, domainsProcessor, operationProcessor);
        this.errorMessageBuilder = errorMessageBuilder;
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
            return constructSuccessSetDataResponse(data, requestInfo, responseInfo);
        } catch (N2oException e) {
            SetDataResponse response = constructFailSetDataResponse(e, requestInfo);
            logger.error("Error response " + response.getStatus() + " " + e.getSeverity(), e);
            return response;
        }
    }

    private SetDataResponse constructFailSetDataResponse(N2oException e, ActionRequestInfo<DataSet> requestInfo) {
        SetDataResponse response = new SetDataResponse();
        if (requestInfo.isMessageOnFail()) {
            String widgetId = requestInfo.getFailAlertWidgetId() == null
                    ? requestInfo.getMessagesForm()
                    : requestInfo.getFailAlertWidgetId();
            response.addResponseMessages(errorMessageBuilder.buildMessages(e), widgetId);
        }
        response.setStatus(e.getHttpStatus());
        return response;
    }


    private SetDataResponse constructSuccessSetDataResponse(DataSet data,
                                                            ActionRequestInfo<DataSet> requestInfo,
                                                            ActionResponseInfo responseInfo) {
        SetDataResponse response = new SetDataResponse();
        response.setResponseMessages(responseInfo.getMessageList(), requestInfo.getSuccessAlertWidgetId(), responseInfo.getStackedMessages());
        response.setData(data);
        if (requestInfo.isMessageOnSuccess())
            response.addResponseMessage(createSuccess(requestInfo.getOperation(), data), requestInfo.getSuccessAlertWidgetId());
        return response;
    }

    private ResponseMessage createSuccess(CompiledObject.Operation operation, DataSet data) {
        ResponseMessage message = new ResponseMessage();
        message.setSeverityType(SeverityType.success);
        message.setText(StringUtils.resolveLinks(operation.getSuccessText(), data));
        message.setData(data);
        return message;
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.operation;
    }

}
