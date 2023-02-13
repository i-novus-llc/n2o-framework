package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента ButtonField
 */

@Component
public class ButtonFieldIOv2 extends FieldIOv2<N2oButtonField> implements ControlIOv2 {

    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oButtonField m, IOProcessor p) {
        super.io(e, m, p);

        p.attribute(e, "widget-id", m::getWidgetId, m::setWidgetId);
        p.attributeEnum(e, "model", m::getModel, m::setModel, ReduxModel.class);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "validate", m::getValidateString, m::setValidateString);
        p.attribute(e, "action-id", m::getActionId, m::setActionId);
        p.attribute(e, "tooltip-position", m::getTooltipPosition, m::setTooltipPosition);
        p.attributeBoolean(e, "rounded", m::getRounded, m::setRounded);

        p.attribute(e, "confirm", m::getConfirm, m::setConfirm);
        p.attribute(e, "confirm-text", m::getConfirmText, m::setConfirmText);
        p.attributeEnum(e, "confirm-type", m::getConfirmType, m::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", m::getConfirmTitle, m::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", m::getConfirmOkLabel, m::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", m::getConfirmCancelLabel, m::setConfirmCancelLabel);

        p.attributeEnum(e, "type", m::getType, m::setType, LabelType.class);
        p.anyChildren(e, null, m::getActions, m::setActions, p.anyOf(N2oAction.class).ignore("dependencies"), actionDefaultNamespace);
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
