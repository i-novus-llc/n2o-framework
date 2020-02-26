package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.AbstractMenuItem;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Чтение\запись содержимого Toolbar
 */
public abstract class AbstractMenuItemIO<T extends AbstractMenuItem> implements NamespaceIO<T> {

    static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/button-1.0");

    protected Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, T mi, IOProcessor p) {
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
        p.attribute(e, "style", mi::getStyle, mi::setStyle);
        p.attribute(e, "enabling-condition", mi::getEnablingCondition, mi::setEnablingCondition);
        p.attribute(e, "visibility-condition", mi::getVisibilityCondition, mi::setVisibilityCondition);
        p.attribute(e, "description", mi::getDescription, mi::setDescription);
        p.anyAttributes(e, mi::getExtAttributes, mi::setExtAttributes);
        p.attribute(e, "tooltip-position", mi::getTooltipPosition, mi::setTooltipPosition);

        p.attributeBoolean(e, "confirm", mi::getConfirm, mi::setConfirm);
        p.attribute(e, "confirm-text", mi::getConfirmText, mi::setConfirmText);
        p.attributeEnum(e, "confirm-type", mi::getConfirmType, mi::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", mi::getConfirmTitle, mi::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", mi::getConfirmOkLabel, mi::setConfirmOkLabel);
        p.attribute(e, "confirm-cancel-label", mi::getConfirmCancelLabel, mi::setConfirmCancelLabel);
        p.anyChildren(e, "dependencies", mi::getDependencies, mi::setDependencies, p.oneOf(AbstractMenuItem.Dependency.class)
                .add("enabling", AbstractMenuItem.EnablingDependency.class, this::dependency)
                .add("visibility", AbstractMenuItem.VisibilityDependency.class, this::dependency));
        p.child(e, null, "copy", mi::getCopy, mi::setCopy, AbstractMenuItem.Copy.class, this::copy);
        p.anyChild(e, null, mi::getAction, mi::setAction, p.anyOf(N2oAction.class).ignore("dependencies", "copy"), actionDefaultNamespace);
    }

    private void dependency(Element e, AbstractMenuItem.Dependency t, IOProcessor p) {
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getRefModel, t::setRefModel, ReduxModel.class);
        p.attributeArray(e, "on", ",", t::getOn, t::setOn);
        p.text(e, t::getValue, t::setValue);
    }

    private void copy(Element e, AbstractMenuItem.Copy c, IOProcessor p) {
        p.attributeEnum(e, "source-model", c::getSourceModel, c::setSourceModel, ReduxModel.class);
        p.attribute(e, "source-widget-id", c::getSourceWidgetId, c::setSourceWidgetId);
        p.attribute(e, "source-field-id", c::getSourceFieldId, c::setSourceFieldId);
        p.attributeEnum(e, "target-model", c::getTargetModel, c::setTargetModel, ReduxModel.class);
        p.attribute(e, "target-widget-id", c::getTargetWidgetId, c::setTargetWidgetId);
        p.attribute(e, "target-field-id", c::getTargetFieldId, c::setTargetFieldId);
        p.attributeEnum(e, "mode", c::getMode, c::setMode, CopyMode.class);
        p.attributeBoolean(e, "close-on-success", c::getCloseOnSuccess, c::setCloseOnSuccess);
    }

    @Override
    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }
}
