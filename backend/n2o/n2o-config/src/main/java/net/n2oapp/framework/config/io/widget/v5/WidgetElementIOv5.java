package net.n2oapp.framework.config.io.widget.v5;


import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oEnablingDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oVisibilityDependency;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.datasource.DatasourceIO;
import net.n2oapp.framework.config.io.toolbar.v2.ToolbarIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись виджета  версии 5.0
 */
public abstract class WidgetElementIOv5<T extends N2oWidget> implements NamespaceIO<T>, WidgetIOv5 {
    private Namespace actionDefaultNamespace = ActionIOv2.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "ref-id", m::getRefId, m::setRefId);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "visible", m::getVisible, m::setVisible);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeBoolean(e, "fetch-on-init", m::getFetchOnInit, m::setFetchOnInit);
        p.attributeBoolean(e, "auto-focus", m::getAutoFocus, m::setAutoFocus);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionsBar::new, this::action);
        p.childAttributeEnum(e, "actions", "generate", m::getActionGenerate, m::setActionGenerate, GenerateType.class);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIOv2());
        p.child(e, null, "datasource", m::getDatasource, m::setDatasource, new DatasourceIO());
        p.anyChildren(e, "dependencies", m::getDependencies, m::setDependencies,
                p.oneOf(N2oDependency.class)
                        .add("visibility", N2oVisibilityDependency.class, this::dependency)
                        .add("enabling", N2oEnablingDependency.class, this::dependency));
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void action(Element e, ActionsBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "name", a::getLabel, a::setLabel);
        p.attribute(e, "icon", a::getIcon, a::setIcon);
        p.attribute(e, "datasource", a::getDatasource, a::setDatasource);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attributeBoolean(e, "default", a::getDefaultValue, a::setDefaultValue);
        p.attribute(e, "visible", a::getVisible, a::setVisible);
        p.attribute(e, "enabled", a::getEnabled, a::setEnabled);
        p.anyChild(e, null, a::getAction, a::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    private void dependency(Element e, N2oDependency t, IOProcessor p) {
        p.attributeArray(e, "on", ",", t::getOn, t::setOn);
        p.attribute(e, "datasource", t::getDatasource, t::setDatasource);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

}
