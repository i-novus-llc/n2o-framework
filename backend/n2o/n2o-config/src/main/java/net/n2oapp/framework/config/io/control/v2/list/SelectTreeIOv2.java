package net.n2oapp.framework.config.io.control.v2.list;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class SelectTreeIOv2 extends ListFieldIOv2<N2oSelectTree> {

    @Override
    public void io(Element e, N2oSelectTree m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "ajax", m::getAjax, m::setAjax);
        p.attributeBoolean(e, "search", m::getSearch, m::setSearch);
        p.attributeBoolean(e, "checkboxes", m::getCheckboxes, m::setCheckboxes);
        p.attribute(e, "parent-field-id", m::getParentFieldId, m::setParentFieldId);
        p.attribute(e, "has-children-field-id", m::getHasChildrenFieldId, m::setHasChildrenFieldId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attributeInteger(e, "size", m::getSize, m::setSize);

        p.child(e, null, "inheritance-nodes", m::getInheritanceNodes, m::setInheritanceNodes, InheritanceNodes.class, this::inheritanceNodes);
    }

    private void inheritanceNodes(Element e, InheritanceNodes m, IOProcessor p) {
        p.attribute(e, "parent-field-id", m::getParentFieldId, m::setParentFieldId);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "has-children-field-id", m::getHasChildrenFieldId, m::setHasChildrenFieldId);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "icon-field-id", m::getIconFieldId, m::setIconFieldId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "search-filter-id", m::getSearchFilterId, m::setSearchFilterId);
        p.attribute(e, "enabled-field-id", m::getEnabledFieldId, m::setEnabledFieldId);
        p.children(e, "pre-filters", "pre-filter", m::getPreFilters, m::setPreFilters, N2oPreFilter::new, this::prefilters);
    }

    private void prefilters(Element e, N2oPreFilter m, IOProcessor p) {
        p.attribute(e, "field-id", m::getFieldId, m::setFieldId);
        p.attribute(e, "value", m::getValueAttr, m::setValueAttr);
        p.attributeEnum(e, "type", m::getType, m::setType, FilterType.class);
        p.attribute(e, "ref", m::getRef, m::setRef);
    }

    @Override
    public Class<N2oSelectTree> getElementClass() {
        return N2oSelectTree.class;
    }

    @Override
    public String getElementName() {
        return "select-tree";
    }
}
