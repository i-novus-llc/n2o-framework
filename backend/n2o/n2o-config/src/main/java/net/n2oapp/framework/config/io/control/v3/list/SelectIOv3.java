package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента выбора из выпадающего списка версии 3.0
 */
@Component
public class SelectIOv3 extends ListFieldIOv3<N2oSelect> {

    @Override
    public void io(Element e, N2oSelect m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "type", m::getType, m::setType, ListType.class);
        p.attributeBoolean(e, "cleanable", m::getCleanable, m::setCleanable);
        p.attribute(e, "select-format", m::getSelectFormat, m::setSelectFormat);
        p.attribute(e, "select-format-one", m::getSelectFormatOne, m::setSelectFormatOne);
        p.attribute(e, "select-format-few", m::getSelectFormatFew, m::setSelectFormatFew);
        p.attribute(e, "select-format-many", m::getSelectFormatMany, m::setSelectFormatMany);
        p.attribute(e, "description-field-id", m::getDescriptionFieldId, m::setDescriptionFieldId);
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
