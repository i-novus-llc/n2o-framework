package net.n2oapp.framework.engine.exception;

import net.n2oapp.framework.api.exception.N2oException;

public class N2oActionException extends N2oException {
    private String objectId;
    private String actionId;

    public N2oActionException(Exception e) {
        super(e);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
