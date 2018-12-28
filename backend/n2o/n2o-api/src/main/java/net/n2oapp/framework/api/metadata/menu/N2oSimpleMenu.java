package net.n2oapp.framework.api.metadata.menu;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

import java.io.Serializable;
import java.util.Map;

/**
 * Простое меню навигации
 */
public class N2oSimpleMenu extends N2oMenu {

    private String src;
    private String refId;
    private String welcomePageId;
    private MenuItem[] menuItems;

    @Override
    public final String getPostfix() {
        return "menu";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oSimpleMenu.class;
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public String getRefId() {
        return refId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getWelcomePageId() {
        return welcomePageId;
    }

    public void setWelcomePageId(String welcomePageId) {
        this.welcomePageId = welcomePageId;
    }

    /**
     * Menu item entity
     */
    public static class MenuItem implements ExtensionAttributesAware, Serializable, Source {
        private String label;
        private String pageId;
        private String href;
        private String icon;
        private String route;
        private Target target;
        private MenuItem[] subMenu;
        private Map<N2oNamespace, Map<String, String>> extAttributes;

        public MenuItem[] getSubMenu() {
            return subMenu;
        }

        public void setSubMenu(MenuItem[] subMenu) {
            this.subMenu = subMenu;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getPageId() {
            return pageId;
        }

        public String getRenderUrl() {
            if (pageId == null && href == null) {
                return "#";
            }
            return pageId != null ? "#".concat(pageId) : href;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }


        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Target getTarget() {
            return target;
        }

        public void setTarget(Target target) {
            this.target = target;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        @Override
        public Map<N2oNamespace, Map<String, String>> getExtAttributes() {
            return extAttributes;
        }

        @Override
        public void setExtAttributes(Map<N2oNamespace, Map<String, String>> extAttributes) {
            this.extAttributes = extAttributes;
        }
    }

    public static class SubMenuItem extends MenuItem {
    }

    public static class PageItem extends MenuItem {
    }

    public static class AnchorItem extends MenuItem {
    }
}




