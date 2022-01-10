package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.meta.page.Dialog;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.DialogContext;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Выполняет действия(action), пришедшие с клиента
 */
@Controller
public class OperationController extends SetController {

    private AlertMessageBuilder messageBuilder;
    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);
    private MetadataEnvironment environment;

    public OperationController(DataProcessingStack dataProcessingStack,
                               N2oOperationProcessor operationProcessor,
                               AlertMessageBuilder messageBuilder,
                               MetadataEnvironment environment) {
        super(dataProcessingStack, operationProcessor, environment);
        this.messageBuilder = messageBuilder;
        this.environment = environment;
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
            logger.error(String.format("Error response %d %s: %s", response.getStatus(), e.getSeverity(),
                    e.getUserMessage() != null ? e.getUserMessage() : e.getMessage()), e);
            return response;
        }
    }

    private SetDataResponse constructFailSetDataResponse(N2oException e, ActionRequestInfo<DataSet> requestInfo) {
        SetDataResponse response = new SetDataResponse();
        if (e.getDialog() != null) {
            response.setDialog(compileDialog(e.getDialog(), requestInfo));
        } else {
            if (requestInfo.isMessageOnFail()) {
                String widgetId = requestInfo.getFailAlertWidgetId() == null
                        ? requestInfo.getMessagesForm()
                        : requestInfo.getFailAlertWidgetId();
                response.addResponseMessages(messageBuilder.buildMessages(e, requestInfo), widgetId);
            }
        }
        response.setStatus(e.getHttpStatus());
        return response;
    }


    private SetDataResponse constructSuccessSetDataResponse(DataSet data,
                                                            ActionRequestInfo<DataSet> requestInfo,
                                                            ActionResponseInfo responseInfo) {
        SetDataResponse response = new SetDataResponse();
        response.setResponseMessages(responseInfo.getMessageList(), requestInfo.getSuccessAlertWidgetId(), responseInfo.isStackedMessages());
        response.setData(data);
        if (responseInfo.getDialog() != null)
            response.setDialog(compileDialog(responseInfo.getDialog(), requestInfo));
        else if (requestInfo.isMessageOnSuccess())
            response.addResponseMessage(
                    messageBuilder.buildSuccessMessage(requestInfo.getOperation().getSuccessText(), requestInfo, data),
                    requestInfo.getSuccessAlertWidgetId());
        return response;
    }

    /**
     * Компиляция диалога подтверждения действия
     *
     * @param n2oDialog   Исходная модель диалога подтверждения действия
     * @param requestInfo Информация о запросе вызова операции
     * @return Клиентская модель диалога подтверждения действия
     */
    private Dialog compileDialog(N2oDialog n2oDialog, ActionRequestInfo<DataSet> requestInfo) {
        String route = requestInfo.getContext().getRoute(null) + normalize(
                n2oDialog.getRoute() != null ? n2oDialog.getRoute() : n2oDialog.getId());
        DialogContext context = new DialogContext(route, n2oDialog.getId());
        ActionContext actionContext = (ActionContext) requestInfo.getContext();
        context.setPathRouteMapping(actionContext.getPathRouteMapping());
        context.setQueryRouteMapping(actionContext.getQueryRouteMapping());
        context.setParentPageId(actionContext.getParentPageId());
        context.setParentSourceDatasourceId(actionContext.getParentSourceDatasourceId());
        context.setParentClientWidgetId(actionContext.getParentClientWidgetId());
        context.setClientWidgetId(actionContext.getSuccessAlertWidgetId());
        if (requestInfo.getObject() != null)
            context.setObjectId(requestInfo.getObject().getId());
        N2oPipelineSupport pipelineSupport = new N2oPipelineSupport(environment);
        Dialog dialog = environment.getCompilePipelineFunction()
                .apply(pipelineSupport)
                .get(n2oDialog, context);
        dialog = environment.getBindPipelineFunction().apply(pipelineSupport)
                .get(dialog, context, requestInfo.getQueryData());
        return dialog;
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.operation;
    }

}
