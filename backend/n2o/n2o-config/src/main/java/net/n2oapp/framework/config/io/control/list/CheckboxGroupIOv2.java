package net.n2oapp.framework.config.io.control.list;

import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGroup;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class CheckboxGroupIOv2 extends ListFieldIOv2<N2oCheckboxGroup> {

    @Override
    public void io(Element e, N2oCheckboxGroup m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "inline", m::getInline, m::setInline);
        p.attribute(e, "type", m::getType, m::setType);
    }

    @Override
    public Class<N2oCheckboxGroup> getElementClass() {
        return N2oCheckboxGroup.class;
    }

    @Override
    public String getElementName() {
        return "checkbox-group";
    }
}
