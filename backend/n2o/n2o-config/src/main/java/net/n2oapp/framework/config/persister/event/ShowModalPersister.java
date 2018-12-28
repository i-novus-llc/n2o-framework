package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет ShowModal в xml-файл как show-modal
 */
@Component
public class ShowModalPersister extends ShowModalPagePersister<N2oShowModal> {
    private static final ShowModalPersister instance = new ShowModalPersister();

    public static ShowModalPersister getInstance() {
        return instance;
    }

    protected ShowModalPersister() {
    }

    public void setShowModal(N2oShowModal showModal, Element root, Element showModalElement, Namespace namespace) {
        if (showModal == null)
            return;
        this.persistShowModal(showModal, showModalElement, namespace);
        super.setShowModal(showModal, root, showModalElement, namespace);
    }

    @Override
    public Element persist(N2oShowModal showModal, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        this.persistShowModal(showModal, root, namespace);
        super.persistShowModalPage(showModal, root, namespace);
        return root;
    }
    
    private void persistShowModal (N2oShowModal showModal, Element showModalElement, Namespace namespace) {
        setAttribute(showModalElement, "target-field-id", showModal.getTargetFieldId());
        setAttribute(showModalElement, "value-field-id", showModal.getValueFieldId());
        setAttribute(showModalElement, "action-id", showModal.getOperationId());
        setAttribute(showModalElement, "refresh-on-close", showModal.getRefreshOnClose());
        if (showModal.getSubmitOperationId() != null) {
            setAttribute(showModalElement, "model", showModal.getUpload().name());
            setAttribute(showModalElement, "create-more", showModal.getCreateMore());

//            setAttribute(showModalElement, "after-submit", showModal.getEdit().getAfterSubmit());
//            setAttribute(showModalElement, "after-cancel", showModal.getEdit().getAfterCancel());
//            setAttribute(showModalElement, "refresh-after-submit", showModal.getRefEdit().getRefreshAfterSubmit());
            setAttribute(showModalElement, "focus-after-submit", showModal.getFocusAfterSubmit());
        }
    }

    @Override
    public Class<N2oShowModal> getElementClass() {
        return N2oShowModal.class;
    }

    @Override
    public String getElementName() {
        return "show-modal";
    }
}
