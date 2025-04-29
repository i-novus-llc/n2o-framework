package net.n2oapp.framework.config.io.application.header;

import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.header.N2oSearchBar;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class N2oSearchBarIOv2 implements TypedElementIO<N2oSearchBar> {
    @Override
    public Class<N2oSearchBar> getElementClass() {
        return N2oSearchBar.class;
    }

    @Override
    public String getElementName() {
        return "search";
    }

    @Override
    public void io(Element e, N2oSearchBar m, IOProcessor p) {
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "filter-field-id", m::getFilterFieldId, m::setFilterFieldId);
        p.attribute(e, "url-field-id", m::getUrlFieldId, m::setUrlFieldId);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "icon-field-id", m::getIconFieldId, m::setIconFieldId);
        p.attribute(e, "description-field-id", m::getDescriptionFieldId, m::setDescriptionFieldId);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, TargetEnum.class);
    }
}
