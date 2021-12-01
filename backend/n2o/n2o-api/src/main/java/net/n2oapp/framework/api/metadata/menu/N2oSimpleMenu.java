package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

import java.util.Map;

/**
 * Простое меню навигации
 */
@Getter
@Setter
public class N2oSimpleMenu extends N2oMenu {

    private String src;
    private String refId;
    private AbstractMenuItem[] menuItems;

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oSimpleMenu.class;
    }

    /**
     * Абстрактный элемент меню
     */
    @Getter
    @Setter
    public static abstract class AbstractMenuItem implements Source, IdAware, ExtensionAttributesAware {
        private String id;
        private String name;
        private String icon;
        private String image;
        private ImageShape imageShape;
        private Map<N2oNamespace, Map<String, String>> extAttributes;
    }

    /**
     * Элемент меню
     */
    @Getter
    @Setter
    public static class MenuItem extends AbstractMenuItem {
        private String badge;
        private String badgeColor;
        private N2oAction action;
    }

    @Getter
    @Setter
    public static class DropdownMenuItem extends AbstractMenuItem {
        private MenuItem[] menuItems;
    }

    @Deprecated
    public static class PageMenuItem extends MenuItem {

        @Deprecated
        public void setPageId(String pageId) {
            getOpenPage().setPageId(pageId);
        }
        @Deprecated
        public String getPageId() {
            return getOpenPage().getPageId();
        }
        @Deprecated
        public void setRoute(String route) {
            getOpenPage().setRoute(route);
        }
        @Deprecated
        public String getRoute() {
            return getOpenPage().getRoute();
        }

        private N2oOpenPage getOpenPage() {
            if (getAction() == null)
                setAction(new N2oOpenPage());
            return (N2oOpenPage) getAction();
        }
    }

    @Deprecated
    public static class AnchorMenuItem extends MenuItem {
        public void setHref(String href) {
            getAnchor().setHref(href);
        }

        public String getHref() {
            return getAnchor().getHref();
        }

        public void setTarget(Target target) {
            getAnchor().setTarget(target);
        }

        public Target getTarget() {
            return getAnchor().getTarget();
        }

        private N2oAnchor getAnchor() {
            if (getAction() == null)
                setAction(new N2oAnchor());
            return (N2oAnchor) getAction();
        }
    }
}




