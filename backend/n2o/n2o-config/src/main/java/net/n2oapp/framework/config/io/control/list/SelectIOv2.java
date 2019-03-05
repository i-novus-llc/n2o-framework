package net.n2oapp.framework.config.io.control.list;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class SelectIOv2 extends ListFieldIOv2<N2oSelect> {

    @Override
    public void io(Element e, N2oSelect m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "type", m::getType, m::setType, ListType.class);
        p.attributeBoolean(e, "cleanable", m::getCleanable, m::setCleanable);
    }

    @Override
    public Class<N2oSelect> getElementClass() {
        return N2oSelect.class;
    }

    @Override
    public String getElementName() {
        return "select";
    }
}
