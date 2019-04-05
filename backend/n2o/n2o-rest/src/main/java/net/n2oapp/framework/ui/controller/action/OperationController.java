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
import org.springframework.stereotype.Controller;

/**
 * Вылняет действие(action) пришедшие с клиента
 */
@Controller
public class OperationController extends SetController {

    private ErrorMessageBuilder errorMessageBuilder;

    public OperationController(DataProcessingStack dataProcessingStack,
                               DomainProcessor domainsProcessor, N2oOperationProcessor operationProcessor, ErrorMessageBuilder errorMessageBuilder) {
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
        SetDataResponse dataWithMessageResponse;
        DataSet data;
        try {
            data = handleActionRequest(requestInfo, responseInfo);
        } catch (N2oException e) {
            String widgetId = requestInfo.getFailAlertWidgetId() == null
                    ? requestInfo.getMessagesForm()
                    : requestInfo.getFailAlertWidgetId();
            SetDataResponse response = new SetDataResponse(errorMessageBuilder.buildMeta(e, widgetId));
            response.setStatus(e.getHttpStatus());
            return response;
        }
        dataWithMessageResponse = constructSuccessSetDataResponse(requestInfo.getOperation(), data,
                requestInfo, responseInfo);
        return dataWithMessageResponse;
    }


    private SetDataResponse constructSuccessSetDataResponse(CompiledObject.Operation operation, DataSet data,
                                                            ActionRequestInfo<DataSet> requestInfo,
                                                            ActionResponseInfo responseInfo) {
        SetDataResponse response = new SetDataResponse(requestInfo.getSuccessAlertWidgetId());
        response.setResponseMessages(responseInfo.getMessageList(), responseInfo.getStackedMessages());
        response.setData(data);
        response.addResponseMessage(createSuccess(operation, data));
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
