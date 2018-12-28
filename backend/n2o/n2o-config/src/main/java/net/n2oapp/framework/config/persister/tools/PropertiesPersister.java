package net.n2oapp.framework.config.persister.tools;

import net.n2oapp.framework.api.metadata.persister.AbstractSimpleElementPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Map;

/**
 * User: operhod
 * Date: 30.01.14
 * Time: 15:29
 */
public class PropertiesPersister extends AbstractSimpleElementPersister<Map<String, Object>> {

    public PropertiesPersister(Namespace namespace) {
        super(namespace);
    }


    @Override
    public Element persist(Map<String, Object> properties, Namespace namespace) {
        Element element = new Element("properties", getNamespace());
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            persist(entry, element);
        }
        return element;
    }

    private void persist(Map.Entry<String, Object> entry, Element parent) {
        Element element = new Element("property", getNamespace());
        PersisterJdomUtil.setAttribute(element, "key", entry.getKey());
        PersisterJdomUtil.setAttribute(element, "value", entry.getValue() != null ? entry.getValue().toString() : null);
        parent.addContent(element);
    }

}
