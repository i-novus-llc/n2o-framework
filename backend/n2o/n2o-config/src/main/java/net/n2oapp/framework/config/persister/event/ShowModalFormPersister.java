package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.global.view.action.control.N2oShowModalForm;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет ShowModalForm в xml-файл как show-modal-form
 */
@Component
public class ShowModalFormPersister extends ShowModalPagePersister<N2oShowModalForm> {

    private static final ShowModalFormPersister instance = new ShowModalFormPersister();

    public static ShowModalFormPersister getInstance() {
        return instance;
    }

    protected ShowModalFormPersister() {
    }

    public void setShowModal(N2oShowModalForm showModal, Element root, Element showModalElement,
                             Namespace namespace) {
        if (showModal == null)
            return;
        persistShowModalForm(showModal, showModalElement);
        super.setShowModal(showModal, root, showModalElement, namespace);
    }

    @Override
    public Element persist(N2oShowModalForm showModal, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        persistShowModalForm(showModal, root);
        super.persistShowModalPage(showModal, root, namespace);
        return root;
    }

    private void persistShowModalForm(N2oShowModalForm showModal, Element showModalElement) {
        setAttribute(showModalElement, "form-id", showModal.getFormId());
        setAttribute(showModalElement, "target-field-id", showModal.getTargetFieldId());
        setAttribute(showModalElement, "value-field-id", showModal.getValueFieldId());
        setAttribute(showModalElement, "action-id", showModal.getOperationId());
        setAttribute(showModalElement, "refresh-on-close", showModal.getRefreshOnClose());
        if (showModal.getSubmitOperationId() != null) {
            setAttribute(showModalElement, "model", showModal.getUpload());
            setAttribute(showModalElement, "create-more", showModal.getCreateMore());
//            setAttribute(showModalElement, "after-submit", showModal.getEdit().getAfterSubmit());
//            setAttribute(showModalElement, "after-cancel", showModal.getEdit().getAfterCancel());
//            setAttribute(showModalElement, "refresh-after-submit", showModal.getEdit().getRefreshAfterSubmit());
            setAttribute(showModalElement, "focus-after-submit", showModal.getFocusAfterSubmit());
        }
    }

    @Override
    public Class<N2oShowModalForm> getElementClass() {
        return N2oShowModalForm.class;
    }

    @Override
    public String getElementName() {
        return "show-modal-form";
    }
}


