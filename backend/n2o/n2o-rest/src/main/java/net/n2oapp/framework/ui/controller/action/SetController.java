package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ControllerTypeAware;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;

/**
 * Абстрактый контроллер записи данных
 */
public abstract class SetController implements ControllerTypeAware {

    private static final String METADATA_FILE_EXTENSION = ".object.xml";
    private DataProcessingStack dataProcessingStack;
    private DomainProcessor domainsProcessor;
    private N2oOperationProcessor actionProcessor;

    public SetController(DataProcessingStack dataProcessingStack,
                         N2oOperationProcessor actionProcessor,
                         MetadataEnvironment environment) {
        this.dataProcessingStack = dataProcessingStack;
        this.domainsProcessor = environment.getDomainProcessor();
        this.actionProcessor = actionProcessor;
    }

    public abstract SetDataResponse execute(ActionRequestInfo requestScope, ActionResponseInfo responseInfo);

    @SuppressWarnings("unchecked")
    protected DataSet handleActionRequest(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo) {
        DataSet inDataSet = requestInfo.getData();
        dataProcessingStack.processAction(requestInfo, responseInfo, inDataSet);
        DataSet resDataSet = null;
        try {
            resDataSet = actionProcessor.invoke(
                    requestInfo.getOperation(),
                    inDataSet,
                    requestInfo.getInParametersMap().values(),
                    requestInfo.getOutParametersMap().values());
        } catch (N2oSpelException e) {
            e.setOperationId(requestInfo.getOperation().getId());
            N2oSpelException n2oSpelException = new N2oSpelException(e, requestInfo.getObject().getId() + METADATA_FILE_EXTENSION);
            dataProcessingStack.processActionError(requestInfo, responseInfo, inDataSet, n2oSpelException);
        } catch (N2oException e) {
            dataProcessingStack.processActionError(requestInfo, responseInfo, inDataSet, e);
            responseInfo.prepare(inDataSet);
        } catch (Exception exception) {
            throw new N2oException(exception);
        }
        dataProcessingStack.processActionResult(requestInfo, responseInfo, resDataSet);
        responseInfo.prepare(inDataSet);
        return resDataSet;
    }
}
