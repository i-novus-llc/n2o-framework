package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись действия подтверждения
 */
@Component
public class ConfirmActionElementIOV2 extends AbstractActionElementIOV2<N2oConfirmAction> {

    @Override
    public void io(Element e, N2oConfirmAction m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e,"title", m::getTitle, m::setTitle);
        p.attribute(e,"text", m::getText, m::setText);
        p.attribute(e,"class", m::getClassName, m::setClassName);
        p.attribute(e,"style", m::getStyle, m::setStyle);
        p.attributeEnum(e,"type", m::getType, m::setType, ConfirmType.class);
        p.attributeBoolean(e,"close-button", m::getCloseButton, m::setCloseButton);
        p.anyChildren(e, null, m::getConfirmButtons, m::setConfirmButtons, p.oneOf(N2oConfirmAction.ConfirmButton.class)
                        .add("ok", N2oConfirmAction.OkButton.class, this::button)
                        .add("cancel", N2oConfirmAction.CancelButton.class, this::button));
    }

    private void button(Element e, N2oConfirmAction.ConfirmButton b, IOProcessor p) {
        p.attribute(e, "label", b::getLabel, b::setLabel);
        p.attribute(e, "color", b::getColor, b::setColor);
        p.attribute(e, "icon", b::getIcon, b::setIcon);
        p.attribute(e,"class", b::getCssClass, b::setCssClass);
        p.attribute(e,"style", b::getStyle, b::setStyle);
    }

    @Override
    public Class<N2oConfirmAction> getElementClass() {
        return N2oConfirmAction.class;
    }

    @Override
    public String getElementName() {
        return "confirm";
    }
}
