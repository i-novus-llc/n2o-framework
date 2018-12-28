package net.n2oapp.framework.api.metadata.local.view.page;

import java.io.Serializable;

/**
 * User: operehod
 * Date: 02.03.2015
 * Time: 10:23
 */
public class PageNav implements Serializable {

    private String pageId;
    private String containerId;
    private String menuItemId;
    private String openingPageId;

    public PageNav(String pageId, String containerId, String menuItemId, String openingPageId) {
        this.pageId = pageId;
        this.containerId = containerId;
        this.menuItemId = menuItemId;
        this.openingPageId = openingPageId;
    }


    public String getPageId() {
        return pageId;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getOpeningPageId() {
        return openingPageId;
    }
}
