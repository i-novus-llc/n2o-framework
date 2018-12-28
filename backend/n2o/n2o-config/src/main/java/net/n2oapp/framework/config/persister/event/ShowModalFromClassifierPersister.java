package net.n2oapp.framework.config.persister.event;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.ShowModalPageFromClassifier;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * User: iryabov
 * Date: 04.07.13
 * Time: 17:21
 */
@Component
public class ShowModalFromClassifierPersister extends ShowModalPagePersister<ShowModalPageFromClassifier> {

    private static final ShowModalFromClassifierPersister instance = new ShowModalFromClassifierPersister();

    private ShowModalFromClassifierPersister() {
    }

    public static ShowModalFromClassifierPersister getInstance() {
        return instance;
    }

    public void setShowModal(ShowModalPageFromClassifier showModal, Element root, Element showModalElement,
                             Namespace namespace) {
        if (showModal == null) return;
        persistShowModalFromClassifier(showModal, showModalElement, namespace);
        super.setShowModal(showModal, root, showModalElement, namespace);
    }

    @Override
    public Element persist(ShowModalPageFromClassifier showModal, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        persistShowModalFromClassifier(showModal, root, namespace);
        super.persistShowModalPage(showModal, root, namespace);
        return root;
    }

    private void persistShowModalFromClassifier (ShowModalPageFromClassifier showModal, Element showModalElement,
                                                 Namespace namespace) {
        setAttribute(showModalElement, "label-field-id", showModal.getLabelFieldId());
        setAttribute(showModalElement, "value-field-id", showModal.getValueFieldId());
    }

    @Override
    public Class<ShowModalPageFromClassifier> getElementClass() {
        return ShowModalPageFromClassifier.class;
    }

    @Override
    public String getElementName() {
        return "show-modal-form";
    }
}
