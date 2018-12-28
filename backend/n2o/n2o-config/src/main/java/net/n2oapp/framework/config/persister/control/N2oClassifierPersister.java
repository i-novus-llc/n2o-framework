package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oClassifier;
import net.n2oapp.framework.config.persister.event.ShowModalFromClassifierPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Запись классифаера
 */
@Component
public class N2oClassifierPersister extends N2oControlXmlPersister<N2oClassifier> {
    @Override
    public Class<N2oClassifier> getElementClass() {
        return N2oClassifier.class;
    }

    @Override
    public Element persist(N2oClassifier entity, Namespace namespace) {
        Element n2oClassifierElement = fillClassifier(entity, namespacePrefix, namespaceUri);
        return n2oClassifierElement;
    }

    public Element fillClassifier(N2oClassifier n2o, String namespacePrefix, String namespaceUri) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element n2oClassifierElement = new Element(getElementName(), namespace);
        setControl(n2oClassifierElement, n2o);
        setField(n2oClassifierElement, n2o);
        setListField(n2oClassifierElement, n2o);
        setListQuery(n2oClassifierElement, n2o);
        setAttribute(n2oClassifierElement, "search-as-you-type", n2o.getSearchAsYouType());
        setAttribute(n2oClassifierElement, "word-wrap", n2o.getWordWrap());
        if (n2o.getShowModal() != null) {
            Element showModal = new Element("show-modal", namespace);
            ShowModalFromClassifierPersister.getInstance().setShowModal(n2o.getShowModal(), n2oClassifierElement, showModal, namespace);
        }
        return n2oClassifierElement;
    }

    @Override
    public String getElementName() {
        return "classifier";
    }
}
