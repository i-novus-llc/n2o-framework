package net.n2oapp.framework.config.io.widget.table.cell;


import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;


/**
 *Чтение\запись ячейки с кнопками.
 */
@Component
public class ToolbarCellElementIOv2 extends AbstractCellElementIOv2<N2oToolbarCell> {
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oToolbarCell  c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeArray(e, "generate", ",", c::getGenerate, c::setGenerate);
        p.anyChildren(e, null, c::getItems, c::setItems, p.oneOf(ToolbarItem.class)
                .add("button", N2oButton.class, this::button)
                .add("sub-menu", N2oSubmenu.class, this::dropdown));
    }

    private void button(Element e, N2oButton b, IOProcessor p) {
        menuItem(e, b, p);
        p.attributeEnum(e, "type", b::getType, b::setType, LabelType.class);
        p.attributeEnum(e, "model", b::getModel, b::setModel, ReduxModel.class);
        p.attribute(e, "widget-id", b::getWidgetId, b::setWidgetId);
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

    private void menuItem(Element e, AbstractMenuItem mi, IOProcessor p) {
        p.attribute(e, "id", mi::getId, mi::setId);
        p.attribute(e, "label", mi::getLabel, mi::setLabel);
        p.attribute(e, "icon", mi::getIcon, mi::setIcon);
        p.attribute(e, "visible", mi::getVisible, mi::setVisible);
        p.attribute(e, "enabled", mi::getEnabled, mi::setEnabled);
        p.attributeBoolean(e, "validate", mi::getValidate, mi::setValidate);
        p.attribute(e, "action-id", mi::getActionId, mi::setActionId);
        p.attribute(e, "class", mi::getClassName, mi::setClassName);
        p.attribute(e, "description", mi::getDescription, mi::setDescription);
        p.attributeBoolean(e,"confirm", mi::getConfirm, mi::setConfirm);
        p.attribute(e, "confirm-text", mi::getConfirmText, mi::setConfirmText);
        p.attributeEnum(e, "confirm-type", mi::getConfirmType, mi::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", mi::getConfirmTitle, mi::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", mi::getConfirmOkLabel, mi::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", mi::getConfirmCancelLabel, mi::setConfirmCancelLabel);
        p.attribute(e, "tooltip-position", mi::getTooltipPosition, mi::setTooltipPosition);
        p.anyAttributes(e, mi::getExtAttributes, mi::setExtAttributes);
    }

    private void submenu(Element e, N2oMenuItem mi, IOProcessor p) {
        menuItem(e, mi, p);
        p.anyChild(e, null, mi::getAction, mi::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    @Override
    public String getElementName() {
        return "toolbar" ;
    }

    @Override
    public Class<N2oToolbarCell> getElementClass() {
        return N2oToolbarCell.class;
    }
}
