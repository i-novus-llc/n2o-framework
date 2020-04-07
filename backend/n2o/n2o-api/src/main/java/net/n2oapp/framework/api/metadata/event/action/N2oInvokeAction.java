package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

import java.io.Serializable;

/**
 * Действие вызова операции
 */
@Getter
@Setter
public class N2oInvokeAction extends N2oAbstractAction {
    private String operationId;
    private String objectId;
    private Boolean closeOnSuccess;
    private Boolean refreshOnSuccess;
    private String confirmationText;
    private String bulkConfirmationText;
    @Deprecated private RefreshPolity refreshPolity;
    private String route;
    private String redirectUrl;
    private Target redirectTarget;
    private String refreshWidgetId;
    private Boolean messageOnSuccess;
    private Boolean messageOnFail;
    private Boolean optimistic;

    private Param[] formParams;
    private Param[] pathParams;
    private Param[] headerParams;

    public N2oInvokeAction() {
    }

    public N2oInvokeAction(String actionId) {
        this.operationId = actionId;
    }

    @Getter
    @Setter
    public static class Param implements Serializable {
        private String name;
        private String value;
    }
}
