package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель кнопки с выпадающим меню.
 */
@Getter
@Setter
public class N2oSubmenu extends N2oComponent implements GroupItem {
    private String id;
    private String className;
    private Boolean visible;
    private String label;
    private String icon;
    private LabelType type;
    private String description;
    private String color;
    private N2oMenuItem[] menuItems;
    private String[] generate;

    @Override
    public List<N2oAction> getActions() {
        List<N2oAction> actions = new ArrayList<>();
        if (menuItems != null) {
            for (N2oMenuItem item : menuItems) {
                actions.add(item.getAction());
            }
        }
        return actions;
    }
}