package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ValidateType;
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
        p.attributeEnum(e, "validate", b::getValidate, b::setValidate, ValidateType.class);
        p.attributeBoolean(e, "rounded", b::getRounded, b::setRounded);
        p.attributeEnum(e, "model", b::getModel, b::setModel, ReduxModel.class);

        p.attributeBoolean(e, "confirm", b::getConfirm, b::setConfirm);
        p.attribute(e, "confirm-text", b::getConfirmText, b::setConfirmText);
        p.attributeEnum(e, "confirm-type", b::getConfirmType, b::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", b::getConfirmTitle, b::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", b::getConfirmOkLabel, b::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", b::getConfirmCancelLabel, b::setConfirmCancelLabel);

        p.attribute(e, "visible", b::getVisible, b::setVisible);
        p.attribute(e, "enabled", b::getEnabled, b::setEnabled);
        p.attribute(e, "enabling-condition", b::getEnablingCondition, b::setEnablingCondition);
        p.attribute(e, "visibility-condition", b::getVisibilityCondition, b::setVisibilityCondition);
        p.attribute(e, "widget-id", b::getWidgetId, b::setWidgetId);
        p.attributeEnum(e, "disable-on-empty-model", b::getDisableOnEmptyModel, b::setDisableOnEmptyModel, DisableOnEmptyModelType.class);
        p.anyChildren(e, "dependencies", b::getDependencies, b::setDependencies, p.oneOf(N2oButton.Dependency.class)
                .add("enabling", N2oButton.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oButton.VisibilityDependency.class, this::dependency));

        p.anyChild(e, null, b::getAction, b::setAction, p.anyOf(N2oAction.class).ignore("dependencies"), actionDefaultNamespace);
    }

    private void dependency(Element e, N2oButton.Dependency t, IOProcessor p) {
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

    private void enablingDependency(Element e, N2oButton.EnablingDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }
}
