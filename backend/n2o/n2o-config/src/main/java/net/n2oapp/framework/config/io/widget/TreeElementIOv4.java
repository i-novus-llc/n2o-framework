package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;

public class TreeElementIOv4 extends WidgetElementIOv4<N2oTree> {

    @Override
    public void io(Element e, N2oTree m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "expand", m::getExpand, m::setExpand);
        p.attributeBoolean(e, "checkboxes", m::getCheckboxes, m::setCheckboxes);
        p.attributeBoolean(e, "auto-select", m::getAutoSelect, m::setAutoSelect);

        p.attribute(e, "parent-field-id", m::getParentFieldId, m::setParentFieldId);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "has-children-field-id", m::getHasChildrenFieldId, m::setHasChildrenFieldId);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "icon-field-id", m::getIconFieldId, m::setIconFieldId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "master-field-id", m::getMasterFieldId, m::setMasterFieldId);
        p.attribute(e, "detail-field-id", m::getDetailFieldId, m::setDetailFieldId);
        p.attribute(e, "search-field-id", m::getSearchFieldId, m::setSearchFieldId);
        p.attribute(e, "enabled-field-id", m::getEnabledFieldId, m::setEnabledFieldId);
    }

    @Override
    public Class<N2oTree> getElementClass() {
        return N2oTree.class;
    }

    @Override
    public String getElementName() {
        return "tree";
    }
}
