package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Чтение\запись тулбара.
 */
public class ToolbarIO implements TypedElementIO<N2oToolbar> {

    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public Class<N2oToolbar> getElementClass() {
        return N2oToolbar.class;
    }

    @Override
    public String getElementName() {
        return "toolbar";
    }

    @Override
    public void io(Element e, N2oToolbar m, IOProcessor p) {
        p.attribute(e, "place", m::getPlace, m::setPlace);
        p.attributeArray(e, "generate", ",", m::getGenerate, m::setGenerate);
        p.anyChildren(e, null, m::getItems, m::setItems, p.oneOf(ToolbarItem.class)
                .add("button", N2oButton.class, this::button)
                .add("sub-menu", N2oSubmenu.class, this::dropdown)
                .add("group", N2oGroup.class, this::group));
    }

    private void button(Element e, N2oButton b, IOProcessor p) {
        menuItem(e, b, p);
        p.attributeEnum(e, "type", b::getType, b::setType, LabelType.class);
        p.attributeEnum(e, "model", b::getModel, b::setModel, ReduxModel.class);
        p.attribute(e, "widget-id", b::getWidgetId, b::setWidgetId);
        p.attributeBoolean(e,"confirm", b::getConfirm, b::setConfirm);
        p.attribute(e, "confirm-text", b::getConfirmText, b::setConfirmText);
        p.attribute(e, "confirm-title", b::getConfirmTitle, b::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", b::getConfirmOkLabel, b::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", b::getConfirmCancelLabel, b::setConfirmCancelLabel);
        p.anyChild(e, null, b::getAction, b::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    private void dropdown(Element e, N2oSubmenu s, IOProcessor p) {
        p.attribute(e, "id", s::getId, s::setId);
        p.attribute(e, "label", s::getLabel, s::setLabel);
        p.attributeBoolean(e, "visible", s::getVisible, s::setVisible);
        p.attribute(e, "description", s::getDescription, s::setDescription);
        p.attribute(e, "icon", s::getIcon, s::setIcon);
        p.attribute(e, "class", s::getClassName, s::setClassName);
        p.attributeEnum(e, "type", s::getType, s::setType, LabelType.class);
        p.attributeArray(e, "generate", ",", s::getGenerate, s::setGenerate);
        p.children(e, null, "menu-item", s::getMenuItems, s::setMenuItems, N2oMenuItem.class, this::submenu);
    }

    private void submenu(Element e, N2oMenuItem mi, IOProcessor p) {
        menuItem(e, mi, p);
        p.anyChild(e, null, mi::getAction, mi::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    private void menuItem(Element e, AbstractMenuItem mi, IOProcessor p) {
        p.attribute(e, "id", mi::getId, mi::setId);
        p.attribute(e, "label", mi::getLabel, mi::setLabel);
        p.attribute(e, "widget-id", mi::getWidgetId, mi::setWidgetId);
        p.attributeEnum(e, "model", mi::getModel, mi::setModel, ReduxModel.class);
        p.attribute(e, "icon", mi::getIcon, mi::setIcon);
        p.attribute(e, "color", mi::getColor, mi::setColor);
        p.attribute(e, "visible", mi::getVisible, mi::setVisible);
        p.attribute(e, "enabled", mi::getEnabled, mi::setEnabled);
        p.attributeBoolean(e, "validate", mi::getValidate, mi::setValidate);
        p.attribute(e, "action-id", mi::getActionId, mi::setActionId);
        p.attribute(e, "class", mi::getClassName, mi::setClassName);
        p.attribute(e, "enabling-condition", mi::getEnablingCondition, mi::setEnablingCondition);
        p.attribute(e, "visibility-condition", mi::getVisibilityCondition, mi::setVisibilityCondition);
        p.attribute(e, "description", mi::getDescription, mi::setDescription);
        p.extensionAttributes(e, mi::getExtAttributes, mi::setExtAttributes);
    }


    private void group(Element e, N2oGroup g, IOProcessor p) {
        p.attributeArray(e, "generate", ",", g::getGenerate, g::setGenerate);
        p.anyChildren(e, null, g::getItems, g::setItems, p.oneOf(GroupItem.class)
                .add("button", N2oButton.class, this::button)
                .add("sub-menu", N2oSubmenu.class, this::dropdown));
    }

}
