package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import org.jdom.Element;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.tools.showModal.N2oStandardShowModalReaderUtil.*;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * Считывает show-modal из xml-файла
 */
@Component
public class ShowModalWithActionReaderV1 extends AbstractN2oEventXmlReader<N2oShowModal> {

    @Override
    public N2oShowModal read(Element element) {
        if (element == null) return null;
        N2oShowModal showModal = new N2oShowModal();
        getShowModalDefinition(element, showModal);
        readEditFromActionId(element, showModal);
        showModal.setRefreshOnClose(ReaderJdomUtil.getAttributeBoolean(element, "refresh-on-close"));
        showModal.setTargetFieldId(getAttributeString(element, "target-field-id"));
        showModal.setValueFieldId(getAttributeString(element, "value-field-id"));
        showModal.setNamespaceUri(element.getNamespaceURI());
        return showModal;
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
