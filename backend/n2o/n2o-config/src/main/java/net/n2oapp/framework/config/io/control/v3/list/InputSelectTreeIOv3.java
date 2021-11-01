package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.list.CheckingStrategy;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента ввода с выбором в выпадающем списке в виде дерева версии 3.0
 */
@Component
public class InputSelectTreeIOv3 extends ListFieldIOv3<N2oInputSelectTree> {

    @Override
    public void io(Element e, N2oInputSelectTree m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "ajax", m::getAjax, m::setAjax);
        p.attributeBoolean(e, "search", m::getSearch, m::setSearch);
        p.attributeBoolean(e, "checkboxes", m::getCheckboxes, m::setCheckboxes);
        p.attributeEnum(e, "checking-strategy", m::getCheckingStrategy, m::setCheckingStrategy, CheckingStrategy.class);
        p.attributeInteger(e, "max-tag-count", m::getMaxTagCount, m::setMaxTagCount);
        p.attribute(e, "parent-field-id", m::getParentFieldId, m::setParentFieldId);
        p.attribute(e, "has-children-field-id", m::getHasChildrenFieldId, m::setHasChildrenFieldId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "master-field-id", m::getMasterFieldId, m::setMasterFieldId);
        p.attribute(e, "detail-field-id", m::getDetailFieldId, m::setDetailFieldId);
        p.attributeInteger(e, "max-tag-text-length", m::getMaxTagTextLength, m::setMaxTagTextLength);
    }

    @Override
    public Class<N2oInputSelectTree> getElementClass() {
        return N2oInputSelectTree.class;
    }

    @Override
    public String getElementName() {
        return "input-select-tree";
    }
}
