package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента радио-группа версии 3.0
 */
@Component
public class RadioGroupIOv3 extends ListFieldIOv3<N2oRadioGroup> {

    @Override
    public void io(Element e, N2oRadioGroup m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "inline", m::getInline, m::setInline);
        p.attributeEnum(e, "type", m::getType, m::setType, N2oRadioGroup.RadioGroupType.class);
    }

    @Override
    public Class<N2oRadioGroup> getElementClass() {
        return N2oRadioGroup.class;
    }

    @Override
    public String getElementName() {
        return "radio-group";
    }
}