package net.n2oapp.framework.config.persister.event;

import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * User: operhod
 * Date: 30.01.14
 * Time: 15:29
 */
@Component
public class AnchorPersister extends N2oEventXmlPersister<N2oAnchor> {
    private static final AnchorPersister instance = new AnchorPersister();

    public static AnchorPersister getInstance() {
        return instance;
    }

    private AnchorPersister() {
    }

    public void setAnchor(N2oAnchor anchor, Element root, Element child, Namespace namespace) {
        if (anchor == null) return;
        setAttribute(child, "href", anchor.getHref());
        setAttribute(child, "target", anchor.getTarget());
        root.addContent(child);
    }

    @Override
    public Element persist(N2oAnchor anchor, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        setAttribute(root, "href", anchor.getHref());
        setAttribute(root, "target", anchor.getTarget());
        return root;
    }

    @Override
    public Class<N2oAnchor> getElementClass() {
        return N2oAnchor.class;
    }

    @Override
    public String getElementName() {
        return "a";
    }
}
