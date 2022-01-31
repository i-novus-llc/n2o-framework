package net.n2oapp.framework.config.io.control.v2.list;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class InputSelectIOv2 extends ListFieldIOv2<N2oInputSelect> {

    @Override
    public void io(Element e, N2oInputSelect m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "type", m::getType, m::setType, ListType.class);
        p.attributeBoolean(e, "reset-on-blur", m::getResetOnBlur, m::setResetOnBlur);
        p.attribute(e, "description-field-id", m::getDescriptionFieldId, m::setDescriptionFieldId);
        p.attributeInteger(e, "max-tag-text-length", m::getMaxTagTextLength, m::setMaxTagTextLength);
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