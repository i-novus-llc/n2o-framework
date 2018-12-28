package net.n2oapp.framework.ui.controller.request;

import net.n2oapp.framework.api.user.UserContext;

import javax.servlet.http.HttpServletRequest;

/**
 * User: operhod
 * Date: 15.08.14
 * Time: 11:35
 */
@Deprecated
public class DataRequest {

    private HttpServletRequest httpServletRequest;
    private UserContext user;
    private String pageId;
    private String containerId;

    public DataRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public UserContext getUser() {
        return user;
    }

    public void setUser(UserContext user) {
        this.user = user;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }
}
