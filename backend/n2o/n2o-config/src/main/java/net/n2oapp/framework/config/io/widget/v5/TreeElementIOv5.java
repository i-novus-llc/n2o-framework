package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись виджета дерево
 */
@Component
public class TreeElementIOv5 extends WidgetElementIOv5<N2oTree> implements BadgeAwareIO<N2oTree> {

    @Override
    public void io(Element e, N2oTree m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "checkboxes", m::getCheckboxes, m::setCheckboxes);
        p.attributeBoolean(e, "ajax", m::getAjax, m::setAjax);
        p.attributeBoolean(e, "multi-select", m::getMultiselect, m::setMultiselect);

        p.attribute(e, "parent-field-id", m::getParentFieldId, m::setParentFieldId);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "has-children-field-id", m::getHasChildrenFieldId, m::setHasChildrenFieldId);
        p.attribute(e, "icon-field-id", m::getIconFieldId, m::setIconFieldId);
        p.attribute(e, "image-field-id", m::getImageFieldId, m::setImageFieldId);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        refBadge(e, m, p);
        p.merge(m,getElementName());
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
