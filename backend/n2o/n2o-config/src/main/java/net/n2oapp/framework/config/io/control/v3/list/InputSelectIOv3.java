package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента выбора из списка версии 3.0
 */
@Component
public class InputSelectIOv3 extends ListFieldIOv3<N2oInputSelect> implements BadgeAwareIO<N2oInputSelect> {

    @Override
    public void io(Element e, N2oInputSelect m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "type", m::getType, m::setType, ListType.class);
        p.attributeBoolean(e, "reset-on-blur", m::getResetOnBlur, m::setResetOnBlur);
        p.attribute(e, "description-field-id", m::getDescriptionFieldId, m::setDescriptionFieldId);
        p.attributeInteger(e, "max-tag-count", m::getMaxTagCount, m::setMaxTagCount);
        p.attributeInteger(e, "max-tag-text-length", m::getMaxTagTextLength, m::setMaxTagTextLength);
        p.attributeInteger(e, "throttle-delay", m::getThrottleDelay, m::setThrottleDelay);
        p.attributeInteger(e, "search-min-length", m::getSearchMinLength, m::setSearchMinLength);
        refBadge(e, m , p);
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
