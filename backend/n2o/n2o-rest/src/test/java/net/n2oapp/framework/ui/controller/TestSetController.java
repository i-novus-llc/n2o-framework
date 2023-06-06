package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.action.SetController;

public class TestSetController extends SetController {

    public TestSetController(DataProcessingStack dataProcessingStack, N2oOperationProcessor actionProcessor,
                             MetadataEnvironment environment) {
        super(dataProcessingStack, actionProcessor, environment);
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.operation;
    }

    @Override
    public SetDataResponse execute(ActionRequestInfo requestScope, ActionResponseInfo responseInfo) {
        return null;
    }

    public DataSet handleActionRequest(ActionRequestInfo requestScope, ActionResponseInfo responseInfo) {
        return super.handleActionRequest(requestScope, responseInfo);
    }
}
