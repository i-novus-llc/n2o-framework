package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.rest.ControllerTypeAware;
import net.n2oapp.framework.api.rest.DataResult;
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
    protected DataResult<DataSet> handleActionRequest(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo) {
        DataSet inDataSet = requestInfo.getData();
        CompiledObject.Operation operation = requestInfo.getOperation();
        try {
            dataProcessingStack.processAction(requestInfo, responseInfo, inDataSet);
            DataSet resDataSet = actionProcessor.invoke(
                    operation,
                    inDataSet,
                    requestInfo.getInParametersMap().values(),
                    requestInfo.getOutParametersMap().values());
            dataProcessingStack.processActionResult(requestInfo, responseInfo, resDataSet);
            responseInfo.prepare(inDataSet);
            return new DataResult<>(resDataSet);
        } catch (N2oException e) {
            try {
                dataProcessingStack.processActionError(requestInfo, responseInfo, inDataSet, e);
            } catch (N2oException exception) {
                return new DataResult(requestInfo.getFailAlertWidgetId(), exception);
            }
            responseInfo.prepare(inDataSet);
            return new DataResult(requestInfo.getFailAlertWidgetId(), e);
        } catch (Exception exception) {
            throw new N2oException(exception);
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
