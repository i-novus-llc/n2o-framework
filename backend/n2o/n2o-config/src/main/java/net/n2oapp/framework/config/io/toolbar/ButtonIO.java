package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кнопки
 */
@Component
public class ButtonIO extends AbstractButtonIO<N2oButton> {
    @Override
    public Class<N2oButton> getElementClass() {
        return N2oButton.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }

    @Override
    public void io(Element e, N2oButton b, IOProcessor p) {
        super.io(e, b, p);
        p.attribute(e, "action-id", b::getActionId, b::setActionId);
        p.attributeBoolean(e, "validate", b::getValidate, b::setValidate);
        p.attributeBoolean(e, "rounded", b::getRounded, b::setRounded);
        p.attributeEnum(e, "model", b::getModel, b::setModel, ReduxModel.class);

        p.attributeBoolean(e, "confirm", b::getConfirm, b::setConfirm);
        p.attribute(e, "confirm-text", b::getConfirmText, b::setConfirmText);
        p.attributeEnum(e, "confirm-type", b::getConfirmType, b::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", b::getConfirmTitle, b::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", b::getConfirmOkLabel, b::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", b::getConfirmCancelLabel, b::setConfirmCancelLabel);

        p.anyChild(e, null, b::getAction, b::setAction, p.anyOf(N2oAction.class).ignore("dependencies"), actionDefaultNamespace);
    }
}
