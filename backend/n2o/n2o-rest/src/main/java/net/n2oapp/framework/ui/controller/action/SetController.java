package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.rest.ControllerTypeAware;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

/**
 * Абстрактый контроллер записи данных
 */
public abstract class SetController implements ControllerTypeAware {

    private DataProcessingStack dataProcessingStack;
    private DomainProcessor domainsProcessor;
    private N2oOperationProcessor actionProcessor;

    public SetController(DataProcessingStack dataProcessingStack, DomainProcessor domainsProcessor, N2oOperationProcessor actionProcessor) {
        this.dataProcessingStack = dataProcessingStack;
        this.domainsProcessor = domainsProcessor;
        this.actionProcessor = actionProcessor;
    }

    public abstract SetDataResponse execute(ActionRequestInfo requestScope, ActionResponseInfo responseInfo);

    @SuppressWarnings("unchecked")
    protected DataSet handleActionRequest(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo) {
        DataSet inDataSet = requestInfo.getData();
        CompiledObject.Operation operation = requestInfo.getOperation();
        dataProcessingStack.processAction(requestInfo, responseInfo, inDataSet);
        try {
            DataSet resDataSet = actionProcessor.invoke(
                    operation,
                    inDataSet,
                    requestInfo.getInParametersMap().values(),
                    requestInfo.getOutParametersMap().values());
            dataProcessingStack.processActionResult(requestInfo, responseInfo, resDataSet);
            responseInfo.prepare(inDataSet);
            return resDataSet;
        } catch (N2oException e) {
            dataProcessingStack.processActionError(requestInfo, responseInfo, inDataSet, e);
            e.setAlertKey(requestInfo.getFailAlertWidgetId());
            responseInfo.prepare(inDataSet);
            throw e;
        } catch (Exception exception) {
            throw new N2oException(exception, requestInfo.getFailAlertWidgetId());
        }
    }

    public void setDataProcessingStack(DataProcessingStack dataProcessingStack) {
        this.dataProcessingStack = dataProcessingStack;
    }

    public void setDomainsProcessor(DomainProcessor domainsProcessor) {
        this.domainsProcessor = domainsProcessor;
    }

    public void setActionProcessor(N2oOperationProcessor actionProcessor) {
        this.actionProcessor = actionProcessor;
    }
}
