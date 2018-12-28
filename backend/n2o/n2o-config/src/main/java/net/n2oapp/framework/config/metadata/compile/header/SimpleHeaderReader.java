package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.header.N2oSimpleHeader;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuReader;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/**
 * Чтение простого хедера
 */
public class SimpleHeaderReader extends AbstractFactoredReader<N2oSimpleHeader> {
    private SimpleMenuReader menuReader = new SimpleMenuReader();

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/simple-header-1.0";
    }

    @Override
    public N2oSimpleHeader read(Element element, Namespace namespace) {
        N2oSimpleHeader header = new N2oSimpleHeader();
        header.setName(getElementString(element, "project-name"));
        header.setSrc(getAttributeString(element, "src"));
        header.setCssClass(getAttributeString(element, "css-class"));
        header.setProjectName(getElementString(element, "project-name"));
        header.setProjectImageSrc(getElementString(element, "project-image-src"));
        header.setNamespaceUri(getNamespaceUri());
        Element menu = element.getChild("menu", namespace);
        if (menu != null) readMenu(menu, namespace, header);
        Element userMenu = element.getChild("user-menu", namespace);
        if (userMenu != null) {
            header.setUserMenuSrc(ReaderJdomUtil.getAttributeString(userMenu, "src"));
            header.setUserContext(ReaderJdomUtil.getAttributeString(userMenu, "username-context"));
            header.setProfilePageId(ReaderJdomUtil.getAttributeString(userMenu, "profile-page-id"));
            header.setLoginUrl(ReaderJdomUtil.getAttributeString(userMenu, "login-url"));
            header.setLogoutUrl(ReaderJdomUtil.getAttributeString(userMenu, "logout-url"));
            header.setRegistrationUrl(ReaderJdomUtil.getAttributeString(userMenu, "registration-url"));
        }
        return header;
    }

    private void readMenu(Element element, Namespace namespace, N2oSimpleHeader header) {
        N2oSimpleMenu menu = menuReader.read(element, namespace);
        header.setMenu(menu);
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
