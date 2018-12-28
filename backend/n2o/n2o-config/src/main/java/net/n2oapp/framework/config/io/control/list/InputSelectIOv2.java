package net.n2oapp.framework.config.io.control.list;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class InputSelectIOv2 extends ListFieldIOv2<N2oInputSelect> {

    @Override
    public void io(Element e, N2oInputSelect m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "type", m::getType, m::setType, ListType.class);
        p.attributeBoolean(e, "store-on-input", m::getStoreOnInput, m::setStoreOnInput);
    }

    @Override
    public Class<N2oInputSelect> getElementClass() {
        return N2oInputSelect.class;
    }

    @Override
    public String getElementName() {
        return "input-select";
    }
}