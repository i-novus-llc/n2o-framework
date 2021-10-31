package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение запись настраиваемого поля версии 3.0
 */
@Component
public class CustomFieldIOv3 extends FieldIOv3<N2oCustomField> {
    private Namespace controlDefaultNamespace = ControlIOv3.NAMESPACE;

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
