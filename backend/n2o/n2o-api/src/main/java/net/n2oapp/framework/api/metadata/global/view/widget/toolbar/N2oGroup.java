package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель группы кнопок
 */
public class N2oGroup implements ToolbarItem {
    private GroupItem[] items;
    private String[] generate;

    public GroupItem[] getItems() {
        return items;
    }

    public void setItems(GroupItem[] items) {
        this.items = items;
    }

    public String[] getGenerate() {
        return generate;
    }

    public void setGenerate(String[] generate) {
        this.generate = generate;
    }

    @Override
    public List<N2oAction> getActions() {
        List<N2oAction> actions = new ArrayList<>();
        if (items != null){
            for (GroupItem item : items) {
                actions.addAll(item.getActions());
            }
        }
        return actions;
    }
}
