package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.config.persister.event.ShowModalFromClassifierPersister;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * User: iryabov
 * Date: 20.11.13
 * Time: 9:47
 */
@Component
public class N2oSelectPersister extends N2oControlXmlPersister<N2oSelect> {
    @Override
    public Element persist(N2oSelect control, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        setOptionsList(element, control.getOptions());
        setAttribute(element, "type", control.getType());
        setAttribute(element, "search-as-you-type", control.getSearchAsYouType());
        setAttribute(element, "word-wrap", control.getWordWrap());
        if (control.getShowModal() != null) {
            Element showModal = new Element("show-modal", Namespace.getNamespace(namespacePrefix, namespaceUri));
            ShowModalFromClassifierPersister.getInstance().setShowModal(control.getShowModal(),
                    element, showModal, Namespace.getNamespace(namespacePrefix, namespaceUri));
        }
        return element;
    }

    @Override
    public Class<N2oSelect> getElementClass() {
        return N2oSelect.class;
    }

    @Override
    public String getElementName() {
        return "select";
    }
}
