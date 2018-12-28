package net.n2oapp.framework.config.io.control.list;

import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class RadioGroupIOv2 extends ListFieldIOv2<N2oRadioGroup> {

    @Override
    public void io(Element e, N2oRadioGroup m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "inline", m::getInline, m::setInline);
        p.attribute(e, "type", m::getType, m::setType);
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