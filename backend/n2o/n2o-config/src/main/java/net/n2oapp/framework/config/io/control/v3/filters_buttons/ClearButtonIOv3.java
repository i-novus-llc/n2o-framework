package net.n2oapp.framework.config.io.control.v3.filters_buttons;

import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oClearButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class ClearButtonIOv3 extends FilterButtonFieldIOv3<N2oClearButton> {

    @Override
    public Class getElementClass() {
        return N2oClearButton.class;
    }

    @Override
    public String getElementName() {
        return "clear-button";
    }

    @Override
    public void io(Element e, N2oClearButton m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeArray(e, "ignore", ",", m::getIgnore, m::setIgnore);
    }
}

