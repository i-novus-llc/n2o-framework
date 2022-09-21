package net.n2oapp.framework.config.io.control.v2.list;

import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
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
        p.attributeInteger(e, "size", m::getSize, m::setSize);
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
