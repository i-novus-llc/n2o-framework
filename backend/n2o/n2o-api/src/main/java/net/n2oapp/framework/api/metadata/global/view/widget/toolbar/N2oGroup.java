package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель группы кнопок
 */
@Getter
@Setter
public class N2oGroup implements ToolbarItem, GroupItems<GroupItem> {
    private GroupItem[] items;
    private String[] generate;
    private String namespaceUri;

    @Override
    public List<N2oAction> getListActions() {
        List<N2oAction> actions = new ArrayList<>();
        if (items != null){
            for (GroupItem item : items) {
                actions.addAll(item.getListActions());
            }
        }
        return actions;
    }
}
