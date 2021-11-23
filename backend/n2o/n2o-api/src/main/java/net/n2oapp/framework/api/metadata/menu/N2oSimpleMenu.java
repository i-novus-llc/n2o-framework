package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

import java.io.Serializable;
import java.util.Map;

/**
 * Простое меню навигации
 */
@Getter
@Setter
public class N2oSimpleMenu extends N2oMenu {

    private String src;
    private String refId;
    private MenuItem[] menuItems;

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oSimpleMenu.class;
    }

    /**
     * Элемент меню
     */
    @Getter
    @Setter
    public static class MenuItem implements ExtensionAttributesAware, Serializable, Source {
        private String id;
        private String label;
        private String badgeColor;
        private String icon;
        private Integer badge;
        private N2oAction action;
        @Deprecated
        private String pageId;
        @Deprecated
        private String href;
        @Deprecated
        private String image;
        @Deprecated
        private String route;
        @Deprecated
        private Target target;
        @Deprecated
        private MenuItem[] subMenu;
        private Map<N2oNamespace, Map<String, String>> extAttributes;
    }

    @Getter
    @Setter
    public static class SubMenuItem extends MenuItem {
        private String id;
        private String name;
        private String image;
        private ImageShape imageShape;
        private MenuItem[] menuItems;
    }

    public static class DividerItem extends MenuItem {
    }

    @Deprecated
    public static class PageItem extends MenuItem {
    }

    @Deprecated
    public static class AnchorItem extends MenuItem {
    }
}




