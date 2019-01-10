package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;

/**
 * Чтение/запись базовых свойств компонента SearchButtons (кнопки фильтра)
 */
public class SearchButtonsIOv2 extends FieldIOv2<N2oSearchButtons>{
    @Override
    public Class<N2oSearchButtons> getElementClass() {
        return N2oSearchButtons.class;
    }

    @Override
    public void io(Element e, N2oSearchButtons m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "searchLabel", m::getSearchLabel, m::setSearchLabel);
        p.attribute(e, "resetLabel", m::getResetLabel, m::setResetLabel);
    }

    @Override
    public String getElementName() {
        return "search-buttons";
    }
}
