package net.n2oapp.framework.config.persister.event;

import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет OpenPage в xml-файл как open-page
 */
@Component
public class OpenPagePersister extends ShowModalPagePersister<N2oOpenPage> {
    
    private static final OpenPagePersister instance = new OpenPagePersister();

    public static OpenPagePersister getInstance() {
        return instance;
    }

    private OpenPagePersister() { }

    public void setShowModal(N2oOpenPage openPage, Element root, Element openPageElement, Namespace namespace) {
        if (openPage == null)
            return;
        persistOpenPage(openPage, openPageElement, namespace);
        super.setShowModal(openPage, root, openPageElement, namespace);
    }

    @Override
    public Element persist(N2oOpenPage openPage, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element root = new Element(getElementName(), namespace);
        persistOpenPage(openPage, root, namespace);
        super.persistShowModalPage(openPage, root, namespace);
        return root;
    }

    private void persistOpenPage(N2oOpenPage openPage, Element openPageElement, Namespace namespace) {
        setAttribute(openPageElement, "target-field-id", openPage.getTargetFieldId());
        setAttribute(openPageElement, "value-field-id", openPage.getValueFieldId());
        setAttribute(openPageElement, "action-id", openPage.getOperationId());
        setAttribute(openPageElement, "refresh-on-close", openPage.getRefreshOnClose());

        if (openPage.getSubmitOperationId() != null) {
            setAttribute(openPageElement, "model", openPage.getUpload());
            setAttribute(openPageElement, "create-more", openPage.getCreateMore());
//            setAttribute(openPageElement, "after-submit", openPage.getEdit().getAfterSubmit());
//            setAttribute(openPageElement, "after-cancel", openPage.getEdit().getAfterCancel());
//            setAttribute(openPageElement, "refresh-after-submit", openPage.getEdit().getRefreshAfterSubmit());
            setAttribute(openPageElement, "focus-after-submit", openPage.getFocusAfterSubmit());
        }
    }

    @Override
    public Class<N2oOpenPage> getElementClass() {
        return N2oOpenPage.class;
    }

    @Override
    public String getElementName() {
        return "open-page";
    }
}
