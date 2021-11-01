package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGroup;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента выбора чекбосков версии 3.0
 */
@Component
public class CheckboxGroupIOv3 extends ListFieldIOv3<N2oCheckboxGroup> {

    @Override
    public void io(Element e, N2oCheckboxGroup m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "inline", m::getInline, m::setInline);
        p.attributeEnum(e, "type", m::getType, m::setType, N2oCheckboxGroup.CheckboxGroupType.class);
    }

    @Override
    public Class<N2oCheckboxGroup> getElementClass() {
        return N2oCheckboxGroup.class;
    }

    @Override
    public String getElementName() {
        return "checkbox-group";
    }
}
