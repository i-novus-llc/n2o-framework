package net.n2oapp.framework.config.io.toolbar.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

public interface ButtonIOAware<T extends Button> {

    default void button(Element e, T b, IOProcessor p, Namespace namespace) {
        p.attribute(e, "action-id",  b::getActionId, b::setActionId);
        p.attribute(e, "validate", b::getValidateString, b::setValidateString);
        p.attributeBoolean(e, "rounded", b::getRounded, b::setRounded);
        p.attributeEnum(e, "model", b::getModel, b::setModel, ReduxModel.class);
        p.attribute(e, "widget-id", b::getWidgetId, b::setWidgetId);

        p.attribute(e, "confirm", b::getConfirm, b::setConfirm);
        p.attribute(e, "confirm-text", b::getConfirmText, b::setConfirmText);
        p.attributeEnum(e, "confirm-type", b::getConfirmType, b::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", b::getConfirmTitle, b::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", b::getConfirmOkLabel, b::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", b::getConfirmCancelLabel, b::setConfirmCancelLabel);

        p.anyChildren(e, null, b::getActions, b::setActions, p.anyOf(N2oAction.class).ignore("dependencies"), namespace);
    }

    default void button(Element e, T b, IOProcessor p, ActionsAwareIO<T> action) {
        action.action(e, b, p, "dependencies");

        p.attributeBoolean(e, "validate", b::getValidate, b::setValidate);
        p.attributeArray(e, "validate-datasources", ",", b::getValidateDatasourceIds, b::setValidateDatasourceIds);

        p.attribute(e, "confirm", b::getConfirm, b::setConfirm);
        p.attribute(e, "confirm-text", b::getConfirmText, b::setConfirmText);
        p.attributeEnum(e, "confirm-type", b::getConfirmType, b::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", b::getConfirmTitle, b::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", b::getConfirmOkLabel, b::setConfirmOkLabel);
        p.attribute(e, "confirm-ok-color", b::getConfirmOkColor, b::setConfirmOkColor);
        p.attribute(e, "confirm-cancel-label", b::getConfirmCancelLabel, b::setConfirmCancelLabel);
        p.attribute(e, "confirm-cancel-color", b::getConfirmCancelColor, b::setConfirmCancelColor);
    }
}
