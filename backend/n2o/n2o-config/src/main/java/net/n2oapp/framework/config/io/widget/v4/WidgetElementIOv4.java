package net.n2oapp.framework.config.io.widget.v4;


import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись виджета  версии 4.0
 */
public abstract class WidgetElementIOv4<T extends N2oWidget> implements NamespaceIO<T>, WidgetIOv4 {
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "ref-id", m::getRefId, m::setRefId);
        p.attribute(e, "master-param", m::getMasterParam, m::setMasterParam);
        p.attribute(e, "depends-on", m::getDependsOn, m::setDependsOn);
        p.attribute(e, "master-field-id", m::getMasterFieldId, m::setMasterFieldId);
        p.attribute(e, "detail-field-id", m::getDetailFieldId, m::setDetailFieldId);
        p.attribute(e, "visibility-condition", m::getDependencyCondition, m::setDependencyCondition);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "visible", m::getVisible, m::setVisible);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attributeInteger(e, "size", m::getSize, m::setSize);
        p.attributeEnum(e, "upload", m::getUpload, m::setUpload, UploadType.class);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeBoolean(e, "fetch-on-init", m::getFetchOnInit, m::setFetchOnInit);
        p.attributeBoolean(e, "auto-focus", m::getAutoFocus, m::setAutoFocus);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionsBar::new, this::action);
        p.childAttributeEnum(e, "actions", "generate", m::getActionGenerate, m::setActionGenerate, GenerateType.class);
        p.childrenByEnum(e, "pre-filters", m::getPreFilters, m::setPreFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterType.class, this::prefilter);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIO());
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void action(Element e, ActionsBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "name", a::getLabel, a::setLabel);
        p.attribute(e, "icon", a::getIcon, a::setIcon);
        p.attribute(e, "widget-id", a::getWidgetId, a::setWidgetId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attributeBoolean(e, "default", a::getDefaultValue, a::setDefaultValue);
        p.attribute(e, "visible", a::getVisible, a::setVisible);
        p.attribute(e, "enabled", a::getEnabled, a::setEnabled);
        p.anyChild(e, null, a::getAction, a::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    private void prefilter(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "param", pf::getParam, pf::setParam);
        p.attributeBoolean(e, "routable", pf::getRoutable, pf::setRoutable);
        p.attribute(e, "value", pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attribute(e, "ref-widget-id", pf::getRefWidgetId, pf::setRefWidgetId);
        p.attributeEnum(e, "ref-model", pf::getModel, pf::setModel, ReduxModel.class);
        p.attributeBoolean(e, "required", pf::getRequired, pf::setRequired);
        p.childrenToStringArray(e, null, "value", pf::getValueList, pf::setValueList);
    }

}
