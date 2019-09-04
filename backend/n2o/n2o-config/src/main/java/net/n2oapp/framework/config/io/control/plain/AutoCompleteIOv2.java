package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class AutoCompleteIOv2 extends PlainFieldIOv2<N2oAutoComplete> {

    @Override
    public void io(Element e, N2oAutoComplete m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "search-filter-id", m::getSearchFilterId, m::setSearchFilterId);
    }

    @Override
    public Class<N2oAutoComplete> getElementClass() {
        return N2oAutoComplete.class;
    }

    @Override
    public String getElementName() {
        return "auto-complete";
    }
}
