package net.n2oapp.framework.api.metadata.global.view.action.control;

import java.io.Serializable;

/**
 * @author iryabov
 * @since 03.04.2015
 */
public class CreateOrUpdate implements Serializable {
    private String createActionId;
    private String updateActionId;

    public String getCreateActionId() {
        return createActionId;
    }

    public void setCreateActionId(String createActionId) {
        this.createActionId = createActionId;
    }

    public String getUpdateActionId() {
        return updateActionId;
    }

    public void setUpdateActionId(String updateActionId) {
        this.updateActionId = updateActionId;
    }
}
