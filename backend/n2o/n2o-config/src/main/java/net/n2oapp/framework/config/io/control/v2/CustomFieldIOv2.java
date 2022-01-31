package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение запись настраиваемого поля
 */
@Component
public class CustomFieldIOv2 extends FieldIOv2<N2oCustomField> {
    private Namespace controlDefaultNamespace = ControlIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oCustomField m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "controls", m::getControls, m::setControls, p.anyOf(N2oComponent.class), controlDefaultNamespace);
    }

    @Override
    public Class<N2oCustomField> getElementClass() {
        return N2oCustomField.class;
    }

    @Override
    public String getElementName() {
        return "field";
    }

}
