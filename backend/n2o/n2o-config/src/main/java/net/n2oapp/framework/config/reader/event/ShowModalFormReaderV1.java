package net.n2oapp.framework.config.reader.event;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.view.action.control.N2oShowModalForm;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.tools.showModal.N2oStandardShowModalReaderUtil.*;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * Считывает show-modal-form из xml-файла
 */
@Component
public class ShowModalFormReaderV1 extends AbstractN2oEventXmlReader<N2oShowModalForm> {

    @Override
    public N2oShowModalForm read(Element element) {
        if (element == null) return null;
        N2oShowModalForm showModal = new N2oShowModalForm();
        getShowModalDefinition(element, showModal);
        getShowModalFormDefenition(element, element.getNamespace(), showModal);
        showModal.setTargetFieldId(getAttributeString(element, "target-field-id"));
        showModal.setValueFieldId(getAttributeString(element, "value-field-id"));
        showModal.setLabelFieldId(getAttributeString(element, "label-field-id"));
        showModal.setNamespaceUri(element.getNamespaceURI());
      return showModal;
    }

    public static N2oShowModalForm getShowModalFormDefenition(Element element, Namespace namespace,
                                                              N2oShowModalForm modal) {
        modal.setRefreshOnClose(ReaderJdomUtil.getAttributeBoolean(element, "refresh-on-close"));
        modal.setFormId(ReaderJdomUtil.getAttributeString(element, "form-id"));
//        modal.setEdit(EditReader.getInstance().read(element.getChild("edit", element.getNamespace())));todo
        return modal;
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
