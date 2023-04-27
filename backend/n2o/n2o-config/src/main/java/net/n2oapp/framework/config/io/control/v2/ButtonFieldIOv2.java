package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOAware;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента ButtonField
 */

@Component
public class ButtonFieldIOv2 extends FieldIOv2<N2oButtonField> implements ControlIOv2, ButtonIOAware<N2oButtonField> {

    private final Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oButtonField m, IOProcessor p) {
        super.io(e, m, p);
        button(e, m, p, actionDefaultNamespace);

        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "tooltip-position", m::getTooltipPosition, m::setTooltipPosition);

        p.attributeEnum(e, "type", m::getType, m::setType, LabelType.class);
    }

    @Override
    public Class<N2oButtonField> getElementClass() {
        return N2oButtonField.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }
}
