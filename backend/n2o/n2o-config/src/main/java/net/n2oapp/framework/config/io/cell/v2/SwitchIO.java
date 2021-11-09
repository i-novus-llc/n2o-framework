package net.n2oapp.framework.config.io.cell.v2;


import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись переключателя
 */
public class SwitchIO implements TypedElementIO<N2oSwitch> {
    @Override
    public Class<N2oSwitch> getElementClass() {
        return N2oSwitch.class;
    }

    @Override
    public String getElementName() {
        return "switch";
    }

    @Override
    public void io(Element e, N2oSwitch s, IOProcessor p) {
        p.attribute(e, "value-field-id", s::getValueFieldId, s::setValueFieldId);
        p.childrenToStringMap(e, null, "case", "value", null, s::getCases, s::setCases);
        p.childrenText(e, "default", s::getDefaultCase, s::setDefaultCase);
    }
}
