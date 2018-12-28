package net.n2oapp.framework.standard.header.persisiter;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import net.n2oapp.framework.standard.header.model.global.N2oBaseHeaderModule;

/**
 * User: operhod
 * Date: 20.03.14
 * Time: 16:35
 */
public class StandardHeaderModulePersister extends AbstractN2oMetadataPersister<N2oBaseHeaderModule> {

    public StandardHeaderModulePersister() {
        super("http://n2oapp.net/framework/config/schema/standard-header-module-1.0", "module");
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
    public Element persist(N2oBaseHeaderModule module, Namespace namespace) {
        Element element = new Element(getElementName(), namespaceUri);
        serialize(module, element);
        return element;
    }

    public void serialize(N2oBaseHeaderModule module, Element element) {
        if (module.getRefId() != null) {
            PersisterJdomUtil.setAttribute(element, "ref-id", module.getRefId());
        } else {
            PersisterJdomUtil.setAttribute(element, "main-page", module.getMainPage());
            PersisterJdomUtil.setAttribute(element, "name", module.getName());
            PersisterJdomUtil.setAttribute(element, "url", module.getUrl());
            if (module.getItems() != null) persistItems(element, module.getItems(), element.getNamespace());
        }
    }

    private void persistItems(Element element, N2oBaseHeaderModule.Item[] items, Namespace namespace) {
        for (N2oBaseHeaderModule.Item item : items) {
            if (item instanceof N2oBaseHeaderModule.Space) element.addContent(persistSpace(
                    (N2oBaseHeaderModule.Space) item, namespace));
            else element.addContent(persistPage(
                    (N2oBaseHeaderModule.Page) item, namespace));
        }
    }

    private Element persistPage(N2oBaseHeaderModule.Page page, Namespace namespace) {
        Element element = new Element("page", namespace);
        PersisterJdomUtil.setAttribute(element, "name", page.getName());
        PersisterJdomUtil.setAttribute(element, "id", page.getId());
        return element;
    }

    private Element persistSpace(N2oBaseHeaderModule.Space space, Namespace namespace) {
        Element element = new Element("space", namespace);
        PersisterJdomUtil.setAttribute(element, "name", space.getName());
        PersisterJdomUtil.setAttribute(element, "id", space.getId());
        PersisterJdomUtil.setAttribute(element, "main-page", space.getMainPage());
        if (space.getPages() != null) for (N2oBaseHeaderModule.Page page : space.getPages()) {
            element.addContent(persistPage(page, namespace));
        }
        return element;
    }

    @Override
    public Class<N2oBaseHeaderModule> getElementClass() {
        return N2oBaseHeaderModule.class;
    }

    @Override
    public String getElementName() {
        return "module";
    }
}
