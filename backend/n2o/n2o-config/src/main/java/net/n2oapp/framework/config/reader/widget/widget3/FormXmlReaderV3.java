package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.config.reader.fieldset.FieldSetReaderUtil;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

@Component
public class FormXmlReaderV3 extends WidgetBaseXmlReaderV3<N2oWidget> {

    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oForm n2oForm = new N2oForm();
        readRef(element, n2oForm);
        getAbstractFormDefinition(element, namespace, n2oForm, readerFactory);
        return n2oForm;
    }

    protected void getAbstractFormDefinition(Element formElement, Namespace namespace, N2oForm n2oForm,
                                                  final ElementReaderFactory readerFactory) {
        readWidgetDefinition(formElement, namespace, n2oForm);
        n2oForm.setItems(FieldSetReaderUtil.getFieldSet(formElement, namespace, readerFactory));
        n2oForm.setModalWidth(ReaderJdomUtil.getElementString(formElement, "modal-width"));
        n2oForm.setLayout(ReaderJdomUtil.getElementString(formElement, "layout"));
    }


    @Override
    public Class<N2oWidget> getElementClass() {
        return N2oWidget.class;
    }

    @Override
    public String getElementName() {
        return "form";
    }
}
