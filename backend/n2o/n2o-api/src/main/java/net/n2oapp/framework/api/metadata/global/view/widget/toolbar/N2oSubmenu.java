package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель кнопки с выпадающим меню.
 */
@Getter
@Setter
public class N2oSubmenu extends N2oAbstractButton implements GroupItem {
    private String[] generate;
    private Boolean showToggleIcon;
    private N2oButton[] menuItems;

    @Override
    public List<N2oAction> getActions() {
        List<N2oAction> actions = new ArrayList<>();
        if (menuItems != null) {
            for (N2oButton item : menuItems) {
                actions.add(item.getAction());
            }
        }
        return actions;
    }
}