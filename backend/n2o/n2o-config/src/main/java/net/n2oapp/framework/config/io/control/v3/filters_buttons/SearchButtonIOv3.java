package net.n2oapp.framework.config.io.control.v3.filters_buttons;


import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oSearchButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class SearchButtonIOv3 extends FilterButtonFieldIOv3<N2oSearchButton> {

    @Override
    public Class getElementClass() {
        return N2oSearchButton.class;
    }

    @Override
    public String getElementName() {
        return "search-button";
    }

    @Override
    public void io(Element e, N2oSearchButton mi, IOProcessor p) {
        super.io(e, mi, p);
    }
}
