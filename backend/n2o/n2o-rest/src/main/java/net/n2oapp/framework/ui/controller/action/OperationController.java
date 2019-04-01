package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.DataResult;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.springframework.stereotype.Controller;

/**
 * Вылняет действие(action) пришедшие с клиента
 */
@Controller
public class OperationController extends SetController {

    public OperationController(DataProcessingStack dataProcessingStack, DomainProcessor domainsProcessor, N2oOperationProcessor operationProcessor) {
        super(dataProcessingStack, domainsProcessor, operationProcessor);
    }


    @Override
    @SuppressWarnings("unchecked")
    public SetDataResponse execute(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo) {
        return executeRequest(requestInfo, responseInfo);
    }

    protected SetDataResponse executeRequest(ActionRequestInfo<DataSet> requestInfo,
                                             ActionResponseInfo responseInfo) {
        SetDataResponse dataWithMessageResponse;
        DataResult<DataSet> dataResult = handleActionRequest(requestInfo, responseInfo);
        if (dataResult.isError())
            return new SetDataResponse(dataResult.getErrorInfo());
        dataWithMessageResponse = constructSuccessSetDataResponse(requestInfo.getOperation(), dataResult.getData(),
                requestInfo, responseInfo);
        dataWithMessageResponse.addResponseMessages(responseInfo.getMessageList());
        return dataWithMessageResponse;
    }


    private SetDataResponse constructSuccessSetDataResponse(CompiledObject.Operation operation, DataSet data,
                                                            ActionRequestInfo<DataSet> requestInfo,
                                                            ActionResponseInfo responseInfo) {
        SetDataResponse response = new SetDataResponse(requestInfo.getSuccessAlertWidgetId());
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
