package net.n2oapp.framework.api.metadata.global.view.action;

import net.n2oapp.framework.api.metadata.control.N2oControl;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.RefreshPolity;

import java.io.Serializable;
import java.util.*;

/**
 * User: iryabov
 * Date: 27.06.13
 * Time: 18:07
 */
@Deprecated
public class N2oActionMenu implements Serializable {
    private Boolean inheritDefault;
    private MenuItemGroup[] menuItemGroups;

    public Boolean isInheritDefault() {
        return inheritDefault;
    }

    public void setInheritDefault(Boolean inheritDefault) {
        this.inheritDefault = inheritDefault;
    }

    public List<MenuItem> getMenuItems() {
        if (menuItemGroups == null)
            return null;
        List<MenuItem> list = new ArrayList<>();
        for (MenuItemGroup group : menuItemGroups) {
            for (AbstractMenuItem menuItem : group.getMenuItems()) {
                if (menuItem instanceof MenuItem) {
                    list.add((MenuItem)menuItem);
                } else {
                    list.addAll(Arrays.asList(((SubMenuItem)menuItem).getMenuItems()));
                }
            }

        }
        return list;
    }

    @Deprecated
    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItemGroups = new MenuItemGroup[]{new MenuItemGroup(menuItems)};
    }

    public MenuItemGroup[] getMenuItemGroups() {
        return menuItemGroups;
    }

    public void setMenuItemGroups(MenuItemGroup[] menuItemGroups) {
        this.menuItemGroups = menuItemGroups;
    }

    public static class MenuItemGroup implements Serializable {

        private AbstractMenuItem[] menuItems;
        private boolean defaultGroup = false;
        private String place;

        public MenuItemGroup() {
        }

        public MenuItemGroup(AbstractMenuItem[] menuItems) {
            this.menuItems = menuItems;
        }

        public MenuItemGroup(AbstractMenuItem[] menuItems, boolean defaultGroup) {
            this.menuItems = menuItems;
            this.defaultGroup = defaultGroup;
        }

        public MenuItemGroup(AbstractMenuItem[] menuItems, boolean defaultGroup, String place) {
            this.menuItems = menuItems;
            this.defaultGroup = defaultGroup;
            this.place = place;
        }

        public AbstractMenuItem[] getMenuItems() {
            return menuItems;
        }

        public void setMenuItems(AbstractMenuItem[] menuItems) {
            this.menuItems = menuItems;
        }

        public boolean isDefaultGroup() {
            return defaultGroup;
        }

        public void setDefaultGroup(boolean defaultGroup) {
            this.defaultGroup = defaultGroup;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }
    }

    public abstract static class AbstractMenuItem extends N2oControl {
        private LabelType type;
        private String icon;
        private String color;

        public AbstractMenuItem() {
        }

        public AbstractMenuItem(String id) {
            super(id);
        }

        public LabelType getType() {
            return type;
        }

        public void setType(LabelType type) {
            this.type = type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    @Deprecated
    public static class MenuItem extends AbstractMenuItem {
        private Map<String, Object> properties;
        private Boolean defaultAction;
        private Boolean primary;
        private Boolean context;
        private String key;
        private Boolean bulk;
        private Boolean validate;
        private Condition[] enablingConditions;
        private Condition[] visibilityConditions;
        private RefreshPolity refreshPolity;
        private N2oAction event;

        public MenuItem() {
        }

        public MenuItem(String id) {
            super(id);
        }

        public Condition[] getEnablingConditions() {
            return enablingConditions;
        }

        public RefreshPolity getRefreshPolity() {
            return refreshPolity;
        }

        public void setRefreshPolity(RefreshPolity refreshPolity) {
            this.refreshPolity = refreshPolity;
        }

        public void setEnablingConditions(Condition[] enablingConditions) {
            this.enablingConditions = enablingConditions;
        }

        public Condition[] getVisibilityConditions() {
            return visibilityConditions;
        }

        public void setVisibilityConditions(Condition[] visibilityConditions) {
            this.visibilityConditions = visibilityConditions;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }


        public Boolean getBulk() {
            return bulk;
        }

        public void setBulk(Boolean bulk) {
            this.bulk = bulk;
        }

        public Boolean getValidate() {
            return validate;
        }

        public void setValidate(Boolean validate) {
            this.validate = validate;
        }

        public Boolean getDefaultAction() {
            return defaultAction;
        }

        public Boolean getContext() {
            return context;
        }

        public N2oAction getEvent() {
            return event;
        }

        public void setEvent(N2oAction event) {
            this.event = event;
        }

        public void setDefaultAction(Boolean defaultAction) {
            this.defaultAction = defaultAction;
        }

        public Boolean getPrimary() {
            return primary;
        }

        public void setPrimary(Boolean primary) {
            this.primary = primary;
        }

        public void setContext(Boolean context) {
            this.context = context;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getProperty(String property) {
            if (properties != null)
                return properties.get(property);
            return null;
        }

        @Deprecated
        public static class Condition implements Serializable {
            private String on;
            private String expression;
            private String tooltip;

            public String getExpression() {
                return expression;
            }

            public void setExpression(String expression) {
                this.expression = expression;
            }

            public String getTooltip() {
                return tooltip;
            }

            public void setTooltip(String tooltip) {
                this.tooltip = tooltip;
            }

            public String getOn() {
                return on;
            }

            public void setOn(String on) {
                this.on = on;
            }
        }
    }

    @Deprecated
    public static class SubMenuItem extends AbstractMenuItem {
        private MenuItem[] menuItems;

        public SubMenuItem() {
        }

        public SubMenuItem(String id) {
            super(id);
        }

        public MenuItem[] getMenuItems() {
            return menuItems;
        }

        public void setMenuItems(MenuItem[] menuItems) {
            this.menuItems = menuItems;
        }
    }
}
