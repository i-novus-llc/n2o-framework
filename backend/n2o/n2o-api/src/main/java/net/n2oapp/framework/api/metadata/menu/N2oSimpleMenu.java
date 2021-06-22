package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
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
        private String label;
        private String pageId;
        private String href;
        private String icon;
        private String image;
        private String route;
        private Target target;
        private MenuItem[] subMenu;
        private Map<N2oNamespace, Map<String, String>> extAttributes;
    }

    public static class SubMenuItem extends MenuItem {
    }

    public static class PageItem extends MenuItem {
    }

    public static class AnchorItem extends MenuItem {
    }
}




