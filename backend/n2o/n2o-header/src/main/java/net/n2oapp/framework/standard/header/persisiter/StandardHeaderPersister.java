package net.n2oapp.framework.standard.header.persisiter;

import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.standard.header.model.global.N2oBaseHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oStandardHeader;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * User: operhod
 * Date: 20.03.14
 * Time: 16:35
 */
public class StandardHeaderPersister extends AbstractN2oMetadataPersister<N2oStandardHeader> {
    private StandardHeaderModulePersister headerModulePersister;

    public StandardHeaderPersister() {
        super("http://n2oapp.net/framework/config/schema/standard-header-1.0", "header");
    }

    public void setHeaderModulePersister(StandardHeaderModulePersister headerModulePersister) {
        this.headerModulePersister = headerModulePersister;
    }

    @Override
    public void setNamespaceUri(String uri) {
        namespaceUri = "http://n2oapp.net/framework/config/schema/standard-header-1.0";
    }

    @Override
    public void setNamespacePrefix(String prefix) {
        namespacePrefix = prefix;
    }

    @Override
    public Element persist(N2oStandardHeader header, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespaceUri);
        Element res = new Element(getElementName(), namespace);
        persistNavigation(res, header, namespace);
        if (header.getUserMenu() != null) persistUserMenu(res, header, namespace);
        return res;
    }

    private void persistUserMenu(Element root, N2oStandardHeader header, Namespace namespace) {
        Element element = new Element("user-menu", namespace);
        setAttribute(element, "src", header.getUserMenu().getSrc());
        setAttribute(element, "username-context", header.getUserMenu().getUsernameContext());
        root.addContent(element);
    }

    private void persistNavigation(Element root, N2oStandardHeader header, Namespace namespace) {
        if (header.getModules() == null && header.getModuleGroups() == null) return;
        Element nav = new Element("navigation", namespace);
        setAttribute(nav, "src", header.getSrcNavigation());
        if (header.getModuleGroups() != null) persistGroups(nav, header.getModuleGroups(), namespace);
        if (header.getModules() != null) persistModules(nav, header.getModules(), namespace);
        root.addContent(nav);
    }

    private void persistGroups(Element root, N2oStandardHeader.ModuleGroup[] moduleGroups, Namespace namespace) {
        for (N2oStandardHeader.ModuleGroup moduleGroup : moduleGroups) {
            Element element = new Element("module-group", namespace);
            setAttribute(element, "id", moduleGroup.getId());
            setAttribute(element, "name", moduleGroup.getName());
            if (moduleGroup.getModules() != null) persistModules(element, moduleGroup.getModules(), namespace);
            root.addContent(element);
        }
    }

    private void persistModules(Element element, N2oHeaderModule[] modules, Namespace namespace) {
        for (N2oHeaderModule module : modules) {
            Element mod = new Element("module", namespace);
            if (module instanceof N2oBaseHeaderModule)
                headerModulePersister.serialize((N2oBaseHeaderModule) module, mod);
            element.addContent(mod);
        }
    }

    @Override
    public Class<N2oStandardHeader> getElementClass() {
        return N2oStandardHeader.class;
    }

    @Override
    public String getElementName() {
        return "header";
    }
}
