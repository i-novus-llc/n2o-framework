package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * Persist simple menu
 *
 * @author igafurov
 * @since 13.04.2017
 */
public class SimpleMenuPersister extends AbstractN2oMetadataPersister<N2oSimpleMenu> {
    public SimpleMenuPersister() {
        super("http://n2oapp.net/framework/config/schema/simple-menu-1.0", "menu");
    }

    @Override
    public void setNamespaceUri(String s) {
        this.namespaceUri = s;
    }

    @Override
    public void setNamespacePrefix(String s) {
        this.namespacePrefix = s;
    }

    @Override
    public Element persist(N2oSimpleMenu menu, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "src", menu.getSrc());
        setChildren(root, menu.getMenuItems(),
                new SubMenuItemPersister(namespace));
        return root;
    }

    /**
     * Persist separate menu based on external namespace
     *
     * @param menu      simple menu
     * @param namespace external namespace
     * @return menu element
     */
    public Element persistSeparateMenu(N2oSimpleMenu menu, Namespace namespace) {
        Element menuElement = new Element("menu", namespace);
        String refId = menu.getRefId();
        if (refId != null) setAttribute(menuElement, "ref-id", menu.getRefId());
        setAttribute(menuElement, "src", menu.getSrc());
        setChildren(menuElement, menu.getMenuItems(),
                new SubMenuItemPersister(namespace));
        return menuElement;
    }

    @Override
    public Class<N2oSimpleMenu> getElementClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public String getElementName() {
        return "menu";
    }

    static private class SubMenuItemPersister implements ElementPersister<N2oSimpleMenu.MenuItem> {
        private Namespace namespace;

        public SubMenuItemPersister(Namespace namespace) {
            this.namespace = namespace;
        }

        @Override
        public Element persist(N2oSimpleMenu.MenuItem menuItem, Namespace namespaceElement) {
            Element element = new Element("temp", namespace);
            setAttribute(element, "label", menuItem.getLabel());
            setAttribute(element, "icon", menuItem.getIcon());
            PersisterJdomUtil.setAttribute(element, "target", menuItem.getTarget());
            if (menuItem.getSubMenu() != null) {
                setChildren(element, null, null, menuItem.getSubMenu(), this);
                element.setName("sub-menu");
            } else {
                if (menuItem.getPageId() != null) {
                    setAttribute(element, "page-id", menuItem.getPageId());
                    element.setName("page");
                }
                if (menuItem.getHref() != null) {
                    setAttribute(element, "href", menuItem.getHref());
                    element.setName("a");
                }
            }
            return element;
        }
    }
}
