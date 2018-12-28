package net.n2oapp.framework.api.metadata.global.view.action.control;

import java.io.Serializable;

/**
 * User: iryabov
 * Date: 27.06.13
 * Time: 18:14
 */
public class Redirect implements Serializable, OperationIdAware {
    private String operationId;
    private String hrefFieldId;
    private Target target;

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setActionId(String operationId) {
        this.operationId = operationId;
    }

    public String getHrefFieldId() {
        return hrefFieldId;
    }

    public void setHrefFieldId(String hrefFieldId) {
        this.hrefFieldId = hrefFieldId;
    }
}
