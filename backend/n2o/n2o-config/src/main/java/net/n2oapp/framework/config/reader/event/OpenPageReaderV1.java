package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.tools.showModal.N2oStandardShowModalReaderUtil.getShowModalDefinition;
import static net.n2oapp.framework.config.reader.tools.showModal.N2oStandardShowModalReaderUtil.readEditFromActionId;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * Считывает open-page из xml-файла
 */
@Component
public class OpenPageReaderV1 extends AbstractN2oEventXmlReader<N2oOpenPage> {

    @Override
    public N2oOpenPage read(Element element) {
        if (element == null) return null;
        N2oOpenPage showModal = new N2oOpenPage();
        getShowModalDefinition(element, showModal);
        readEditFromActionId(element, showModal);
        showModal.setRefreshOnClose(ReaderJdomUtil.getAttributeBoolean(element, "refresh-on-close"));
        showModal.setTargetFieldId(getAttributeString(element, "target-field-id"));
        showModal.setValueFieldId(getAttributeString(element, "value-field-id"));
        showModal.setLabelFieldId(getAttributeString(element, "label-field-id"));
        showModal.setNamespaceUri(element.getNamespaceURI());
        return showModal;
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
