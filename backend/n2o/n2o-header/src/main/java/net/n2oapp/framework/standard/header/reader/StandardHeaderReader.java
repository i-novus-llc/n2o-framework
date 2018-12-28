package net.n2oapp.framework.standard.header.reader;

import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.standard.header.model.global.N2oHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oStandardHeader;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * User: operhod
 * Date: 13.01.14
 * Time: 15:16
 */
public class StandardHeaderReader extends AbstractFactoredReader<N2oStandardHeader> {
    private StandardHeaderModuleReader headerModuleReader = new StandardHeaderModuleReader();

    @Override
    @SuppressWarnings("unchecked")
    public N2oStandardHeader read(Element element, Namespace namespace) {
        N2oStandardHeader res = new N2oStandardHeader();
        readNavigation(element.getChild("navigation", namespace), res, namespace);
        readUserMenu(element.getChild("user-menu", namespace), namespace, res);
        res.setNamespaceUri(getNamespaceUri());
        res.setUserContextStructure(UserContextStructureReader.getInstance().read(element.getChild("user-context", namespace)));
        return res;
    }

    private void readUserMenu(Element userMenuElement, Namespace namespace, N2oStandardHeader res) {
        if (userMenuElement == null) return;
        N2oStandardHeader.UserMenu userMenu = new N2oStandardHeader.UserMenu();
        userMenu.setSrc(getAttributeString(userMenuElement, "src"));
        userMenu.setUsernameContext(getAttributeString(userMenuElement, "username-context"));
        res.setUserMenu(userMenu);
    }

    private void readNavigation(Element nav, N2oStandardHeader res, Namespace namespace) {
        if (nav == null) return;
        res.setSrcNavigation(getAttributeString(nav, "src"));
        res.setMainPage(getAttributeString(nav, "main-page"));
        res.setModules(readModules(nav.getChildren("module", namespace), namespace));
        res.setModuleGroups(readGroups(nav.getChildren("module-group", namespace), namespace));
    }

    private N2oStandardHeader.ModuleGroup[] readGroups(List<Element> groups, Namespace namespace) {
        if (groups == null || groups.size() < 1) return null;
        N2oStandardHeader.ModuleGroup[] res = new N2oStandardHeader.ModuleGroup[groups.size()];
        int i = 0;
        for (Element el : groups) {
            N2oStandardHeader.ModuleGroup group = new N2oStandardHeader.ModuleGroup();
            group.setId(getAttributeString(el, "id"));
            group.setName(getAttributeString(el, "name"));
            group.setModules(readModules(el.getChildren("module", namespace), namespace));
            res[i++] = group;
        }
        return res;
    }

    private N2oHeaderModule[] readModules(List<Element> modules, Namespace namespace) {
        if (modules == null || modules.size() < 1) return null;
        N2oHeaderModule[] res = new N2oHeaderModule[modules.size()];
        int i = 0;
        for (Element el : modules) {
            res[i++] = headerModuleReader.read(el, namespace);
        }
        return res;
    }


    @Override
    public Class<N2oStandardHeader> getElementClass() {
        return N2oStandardHeader.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/standard-header-1.0";
    }

    @Override
    public String getElementName() {
        return "header";
    }
}
