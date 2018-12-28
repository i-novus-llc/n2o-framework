package net.n2oapp.framework.config.io.widget.form;

import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ControlIOv2;
import net.n2oapp.framework.config.io.fieldset.FieldsetIOv4;
import net.n2oapp.framework.config.io.widget.WidgetElementIOv4;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись таблицы
 */
@Component
public class FormElementIOV4 extends WidgetElementIOv4<N2oForm> {

    @Override
    public void io(Element e, N2oForm f, IOProcessor p) {
        super.io(e, f, p);
        p.attributeEnum(e, "upload", f::getUpload, f::setUpload, UploadType.class);
        p.attributeEnum(e, "mode", f::getMode, f::setMode, FormMode.class);
        p.attribute(e, "default-values-query-id", f::getDefaultValuesQueryId, f::setDefaultValuesQueryId);
        p.anyChildren(e, "fields", f::getItems, f::setItems, p.anyOf(), FieldsetIOv4.NAMESPACE, ControlIOv2.NAMESPACE);
    }

    @Override
    public String getElementName() {
        return "form";
    }

    @Override
    public Class<N2oForm> getElementClass() {
        return N2oForm.class;
    }
}
