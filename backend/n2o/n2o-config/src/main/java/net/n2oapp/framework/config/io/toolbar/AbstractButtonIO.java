package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись содержимого Toolbar
 */
public abstract class AbstractButtonIO<T extends N2oAbstractButton> extends ComponentIO<T> {

    static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/button-1.0");

    protected Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, T mi, IOProcessor p) {
        super.io(e, mi, p);
        p.attribute(e, "id", mi::getId, mi::setId);
        p.attribute(e, "label", mi::getLabel, mi::setLabel);
        p.attribute(e, "icon", mi::getIcon, mi::setIcon);
        p.attributeEnum(e, "type", mi::getType, mi::setType, LabelType.class);
        p.attribute(e, "color", mi::getColor, mi::setColor);
        p.attribute(e, "visible", mi::getVisible, mi::setVisible);
        p.attribute(e, "enabled", mi::getEnabled, mi::setEnabled);
        p.attribute(e, "enabling-condition", mi::getEnablingCondition, mi::setEnablingCondition);
        p.attribute(e, "visibility-condition", mi::getVisibilityCondition, mi::setVisibilityCondition);
        p.attribute(e, "description", mi::getDescription, mi::setDescription);
        p.attribute(e, "tooltip-position", mi::getTooltipPosition, mi::setTooltipPosition);
        p.attribute(e, "widget-id", mi::getWidgetId, mi::setWidgetId);
        p.attributeEnum(e, "disable-on-empty-model", mi::getDisableOnEmptyModel, mi::setDisableOnEmptyModel, DisableOnEmptyModelType.class);
        p.anyChildren(e, "dependencies", mi::getDependencies, mi::setDependencies, p.oneOf(N2oAbstractButton.Dependency.class)
                .add("enabling", N2oAbstractButton.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oAbstractButton.VisibilityDependency.class, this::dependency));
    }

    private void dependency(Element e, N2oAbstractButton.Dependency t, IOProcessor p) {
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getRefModel, t::setRefModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

    private void enablingDependency(Element e, N2oAbstractButton.EnablingDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }

    @Override
    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }
}
