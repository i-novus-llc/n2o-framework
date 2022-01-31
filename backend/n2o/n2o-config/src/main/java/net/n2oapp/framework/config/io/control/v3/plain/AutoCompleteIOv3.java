package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.control.plain.N2oAutoComplete;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Чтение/запись компонента ввода текста с автоподбором версии 3.0
 */
@Component
public class AutoCompleteIOv3 extends PlainFieldIOv3<N2oAutoComplete> {

    @Override
    public void io(Element e, N2oAutoComplete m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "search-filter-id", m::getSearchFilterId, m::setSearchFilterId);
        p.attributeBoolean(e, "tags", m::getTags, m::setTags);
        p.children(e, "options", "option", m::getOptions, m::setOptions, HashMap::new, this::option);
        p.childrenByEnum(e, "filters", m::getPreFilters, m::setPreFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterType.class, this::prefilter);
        p.attributeInteger(e, "max-tag-text-length", m::getMaxTagTextLength, m::setMaxTagTextLength);
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
