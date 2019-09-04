package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AutoCompleteIOv2 extends PlainFieldIOv2<N2oAutoComplete> {

    @Override
    public void io(Element e, N2oAutoComplete m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "search-filter-id", m::getSearchFilterId, m::setSearchFilterId);
        p.children(e, "options", "option", m::getOptions, m::setOptions, HashMap::new, this::option);
    }

    private void option(Element e, Map<String, String> map, IOProcessor p) {
        p.otherAttributes(e, Namespace.NO_NAMESPACE, map);
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
