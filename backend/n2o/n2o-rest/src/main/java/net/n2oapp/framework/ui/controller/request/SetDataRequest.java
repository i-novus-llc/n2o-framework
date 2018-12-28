package net.n2oapp.framework.ui.controller.request;

import javax.servlet.http.HttpServletRequest;

/**
 * User: operhod
 * Date: 03.06.14
 * Time: 16:23
 */
@Deprecated
public class SetDataRequest extends DataRequest {

    private String objectId;
    private String actionId;
    private Object data;
    private String choice;

    public SetDataRequest(HttpServletRequest httpServletRequest, Object data) {
        super(httpServletRequest);
        this.data = data;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
