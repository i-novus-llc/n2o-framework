package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.control.N2oControl;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель кнопки с выпадающим меню.
 */
public class N2oSubmenu extends N2oControl implements GroupItem {
    private String icon;
    private LabelType type;
    private String color;
    private String className;
    private N2oMenuItem[] menuItems;
    private Boolean visible;
    private String[] generate;

    public N2oMenuItem[] getMenuItems() {
        return menuItems;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMenuItems(N2oMenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LabelType getType() {
        return type;
    }

    public void setType(LabelType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public Boolean getVisible() {
        return visible;
    }

    @Override
    public void setVisible(Boolean visible) {
        this.visible = visible;
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
        if (menuItems != null) {
            for (N2oMenuItem item : menuItems) {
                actions.add(item.getAction());
            }
        }
        return actions;
    }
}