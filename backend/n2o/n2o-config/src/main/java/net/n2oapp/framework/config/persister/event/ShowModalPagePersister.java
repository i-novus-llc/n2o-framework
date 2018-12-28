package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.global.view.action.control.N2oShowModalForm;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.config.persister.tools.PreFilterPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * User: iryabov
 * Date: 04.07.13
 * Time: 17:21
 */
public abstract class ShowModalPagePersister<T extends N2oAbstractPageAction> extends N2oEventXmlPersister<T> {
    protected void setShowModal(N2oAbstractPageAction showModal, Element root, Element showModalElement, Namespace namespace) {
        if (showModal == null) return;
        persistShowModalPage(showModal, showModalElement, namespace);
        root.addContent(showModalElement);
    }

    protected void persistShowModalPage (N2oAbstractPageAction showModal, Element showModalElement, Namespace namespace) {
        PersisterJdomUtil.setAttribute(showModalElement, "page-id", showModal.getPageId());
        if (showModal instanceof N2oShowModalForm) {
            PersisterJdomUtil.setAttribute(showModalElement, "form-id", ((N2oShowModalForm) showModal).getFormId());
        }
        PersisterJdomUtil.setAttribute(showModalElement, "container-id", showModal.getContainerId());
        PersisterJdomUtil.setAttribute(showModalElement, "result-container-id", showModal.getResultContainerId());
        PersisterJdomUtil.setAttribute(showModalElement, "master-field-id", showModal.getMasterFieldId());
        PersisterJdomUtil.setAttribute(showModalElement, "detail-field-id", showModal.getDetailFieldId());
        PersisterJdomUtil.setAttribute(showModalElement, "width", showModal.getWidth());
        PersisterJdomUtil.setAttribute(showModalElement, "page-name", showModal.getPageName());
        PersisterJdomUtil.setAttribute(showModalElement, "max-width", showModal.getMaxWidth());
        PersisterJdomUtil.setAttribute(showModalElement, "min-width", showModal.getMinWidth());
        PreFilterPersister.setPreFilter(showModal.getPreFilters(), showModalElement, namespace);
    }
}
