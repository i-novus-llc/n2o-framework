package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v2.FieldIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента SearchButtons (кнопки фильтра) версии 3.0
 */
@Component
public class SearchButtonsIOv3 extends FieldIOv3<N2oSearchButtons>{
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
