package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.header.N2oSimpleHeader;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuPersister;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * @author V. Alexeev.
 */
public class SimpleHeaderPersister extends AbstractN2oMetadataPersister<N2oSimpleHeader> {
    private SimpleMenuPersister menuPersister = new SimpleMenuPersister();

    public SimpleHeaderPersister() {
        super("http://n2oapp.net/framework/config/schema/simple-header-1.0", "header");
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
    public Element persist(N2oSimpleHeader header, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "src", header.getSrc());
        setAttribute(root, "css-class", header.getCssClass());
        setElementString(root, "project-name", header.getProjectName());
        setElementString(root, "project-image-src", header.getProjectImageSrc());
        if (header.getMenu() != null) persistMenu(root, header.getMenu(), namespace);
        if (header.getUserContext() != null || header.getUserMenuSrc() != null) {
            Element menu = setEmptyElement(root, "user-menu");
            setAttribute(menu, "username-context", header.getUserContext());
            setAttribute(menu, "profile-page-id", header.getProfilePageId());
            setAttribute(menu, "src", header.getUserMenuSrc());
            setAttribute(menu, "login-url", header.getLoginUrl());
            setAttribute(menu, "logout-url", header.getLogoutUrl());
            setAttribute(menu, "registration-url", header.getRegistrationUrl());
        }
        return root;
    }

    private void persistMenu(Element element, N2oSimpleMenu menu, Namespace namespace) {
        Element persistMenu = menuPersister.persistSeparateMenu(menu, namespace);
        element.addContent(persistMenu);
    }

    @Override
    public Class<N2oSimpleHeader> getElementClass() {
        return N2oSimpleHeader.class;
    }

    @Override
    public String getElementName() {
        return "header";
    }
}