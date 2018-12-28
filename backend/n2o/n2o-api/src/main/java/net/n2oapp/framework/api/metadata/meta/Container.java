package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.io.Serializable;

/**
 * Клиентская модель контейнера n2o
 */
@Deprecated
public class Container implements Serializable {
    @JsonProperty
    private String id;
    @JsonProperty
    private String pageId;
    @JsonProperty
    private String icon;
    @JsonProperty
    private boolean opened;
    @JsonProperty
    private boolean fetchOnInit;
    @JsonProperty
    private Widget widget;


    public Container(String id, String pageId) {
        this.id = id;
        this.pageId = pageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isFetchOnInit() {
        return fetchOnInit;
    }

    public void setFetchOnInit(boolean fetchOnInit) {
        this.fetchOnInit = fetchOnInit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
