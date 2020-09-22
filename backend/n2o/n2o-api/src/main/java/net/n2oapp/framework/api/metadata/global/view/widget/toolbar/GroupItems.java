package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

public interface GroupItems<T extends ToolbarItem> {
    void setItems(T[] items);
    T[] getItems();
}
