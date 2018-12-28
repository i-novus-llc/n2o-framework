package net.n2oapp.framework.api.metadata.event.action;

import net.n2oapp.framework.api.metadata.global.view.action.control.OperationIdAware;

/**
 * Событие <invoke-action-and-close>
 */
public class N2oActionAndClose extends N2oAbstractAction implements OperationIdAware, N2oAction {
    private String operationId;
    private Boolean confirmation;
    //todo:добавить src

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    @Override
    public String getOperationId() {
        return operationId;
    }

    @Override
    public String getSrc() {
        return "n2o/controls/action/states/invokeAndClose.state";
    }
}
