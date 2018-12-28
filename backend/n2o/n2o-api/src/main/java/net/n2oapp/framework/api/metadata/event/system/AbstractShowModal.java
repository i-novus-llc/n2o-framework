package net.n2oapp.framework.api.metadata.event.system;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;

/**
 * @author operehod
 * @since 22.10.2015
 */
public abstract class AbstractShowModal extends N2oAbstractAction implements N2oAction, PageIdAwareCompileAction {

    private String pageId;
    private String containerId;
    private String detailFieldId;
    private RefreshPolity refreshPolity;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getDetailFieldId() {
        return detailFieldId;
    }

    public void setDetailFieldId(String detailFieldId) {
        this.detailFieldId = detailFieldId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public RefreshPolity getRefreshPolity() {
        return refreshPolity;
    }

    public void setRefreshPolity(RefreshPolity refreshPolity) {
        this.refreshPolity = refreshPolity;
    }
}
