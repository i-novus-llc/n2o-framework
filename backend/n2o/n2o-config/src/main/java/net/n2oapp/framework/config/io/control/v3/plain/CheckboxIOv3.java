package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.CheckboxDefaultValueEnum;
import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение запись чекбокса версии 3.0
 */
@Component
public class CheckboxIOv3 extends PlainFieldIOv3<N2oCheckbox> {
    @Override
    public Class getElementClass() {
        return N2oCheckbox.class;
    }

    @Override
    public String getElementName() {
        return "checkbox";
    }

    @Override
    public void io(Element e, N2oCheckbox m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "unchecked", m::getUnchecked, m::setUnchecked, CheckboxDefaultValueEnum.class);
    }
}
