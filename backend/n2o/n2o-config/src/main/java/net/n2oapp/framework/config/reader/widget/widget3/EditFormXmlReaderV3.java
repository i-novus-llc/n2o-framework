package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

@Component
public class EditFormXmlReaderV3 extends FormXmlReaderV3 {

    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oForm n2oForm = new N2oForm();
        readRef(element, n2oForm);
        getAbstractFormDefinition(element, namespace, n2oForm, readerFactory);
        n2oForm.setUpload(ReaderJdomUtil.getElementEnum(element, "model", UploadType.class));
        return n2oForm;
    }

    @Override
    public String getElementName() {
        return "edit-form";
    }

}
