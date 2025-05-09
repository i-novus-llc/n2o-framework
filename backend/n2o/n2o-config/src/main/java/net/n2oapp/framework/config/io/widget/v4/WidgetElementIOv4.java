package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись виджета версии 4.0
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
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "visible", m::getVisible, m::setVisible);
        p.attribute(e, "query-id", m::getQueryId, m::setQueryId);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attributeBoolean(e, "fetch-on-init", m::getFetchOnInit, m::setFetchOnInit);
        p.attributeInteger(e, "size", m::getSize, m::setSize);
        p.attributeEnum(e, "upload", m::getUpload, m::setUpload, DefaultValuesModeEnum.class);
        p.attributeBoolean(e, "auto-focus", m::getAutoFocus, m::setAutoFocus);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionBar::new, this::action);
        p.childrenByEnum(e, "pre-filters", m::getPreFilters, m::setPreFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterTypeEnum.class, this::prefilter);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIO());
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
        m.adapterV4();
    }

    private void action(Element e, ActionBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.anyChildren(e, null, a::getN2oActions, a::setN2oActions, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    private void prefilter(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "param", pf::getParam, pf::setParam);
        p.attributeBoolean(e, "routable", pf::getRoutable, pf::setRoutable);
        p.attribute(e, "value", pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attribute(e, "ref-widget-id", pf::getRefWidgetId, pf::setRefWidgetId);
        p.attributeEnum(e, "ref-model", pf::getModel, pf::setModel, ReduxModelEnum.class);
        p.attributeBoolean(e, "required", pf::getRequired, pf::setRequired);
        p.childrenToStringArray(e, null, "value", pf::getValueList, pf::setValueList);
    }

}
