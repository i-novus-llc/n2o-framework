package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Действие вызова операции
 */
@Getter
@Setter
public class N2oInvokeAction extends N2oAbstractAction {
    private String operationId;
    private String objectId;
    private Boolean closeAfterSuccess;
    private Boolean refreshOnSuccess;
    private String confirmationText;
    private String bulkConfirmationText;
    @Deprecated private RefreshPolity refreshPolity;
    private String route;
    private String redirectUrl;
    private Target redirectTarget;

    public N2oInvokeAction() {
    }

    public N2oInvokeAction(String actionId) {
        this.operationId = actionId;
    }
}
