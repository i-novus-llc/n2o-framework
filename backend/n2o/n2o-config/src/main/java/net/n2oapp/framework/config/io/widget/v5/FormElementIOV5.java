package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import net.n2oapp.framework.config.io.fieldset.v5.FieldsetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись виджета Форма версии 5.0
 */
@Component
public class FormElementIOV5 extends WidgetElementIOv5<N2oForm> {

    @Override
    public void io(Element e, N2oForm f, IOProcessor p) {
        super.io(e, f, p);
        p.attributeBoolean(e, "unsaved-data-prompt", f::getPrompt, f::setPrompt);
        p.attributeEnum(e, "mode", f::getMode, f::setMode, FormMode.class);
        p.anyChildren(e, "fields", f::getItems, f::setItems, p.anyOf(SourceComponent.class), FieldsetIOv5.NAMESPACE, ControlIOv3.NAMESPACE);
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
