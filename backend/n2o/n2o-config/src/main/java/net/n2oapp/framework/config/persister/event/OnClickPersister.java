package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.OnClick;
import net.n2oapp.framework.config.persister.tools.PropertiesPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет событие on-click в xml-файл
 */
@Component
public class OnClickPersister extends N2oEventXmlPersister<OnClick> {

    private static final OnClickPersister  instance = new OnClickPersister ();

    public OnClickPersister() {

    }

    public static OnClickPersister  getInstance() {
        return instance;
    }

    @Override
    public Element persist(OnClick onClick, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "function-name", onClick.getFunctionName());
        setAttribute(root, "src", onClick.getSourceSrc());
        if (onClick.getProperties() != null) {
            PropertiesPersister propertiesPersister = new PropertiesPersister(namespace);
            root.addContent(propertiesPersister.persist(onClick.getProperties(),namespace));
        }
        return root;
    }

    @Override
    public Class<OnClick> getElementClass() {
        return OnClick.class;
    }

    @Override
    public String getElementName() {
        return "on-click";
    }
}
