package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента SearchButtons (кнопки фильтра)
 */
@Component
public class SearchButtonsIOv2 extends FieldIOv2<N2oSearchButtons>{
    @Override
    public Class<N2oSearchButtons> getElementClass() {
        return N2oSearchButtons.class;
    }

    @Override
    public void io(Element e, N2oSearchButtons m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "search-label", m::getSearchLabel, m::setSearchLabel);
        p.attribute(e, "reset-label", m::getResetLabel, m::setResetLabel);
        p.attribute(e, "clear-ignore", m::getClearIgnore, m::setClearIgnore);
    }

    @Override
    public String getElementName() {
        return "search-buttons";
    }
}
