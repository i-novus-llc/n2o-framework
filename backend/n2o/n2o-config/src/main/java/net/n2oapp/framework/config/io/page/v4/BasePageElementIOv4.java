package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.control.SubmitOn;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.region.v3.RegionIOv3;
import net.n2oapp.framework.config.io.toolbar.v2.ToolbarIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись базовой страницы версии 4.0
 */
public abstract class BasePageElementIOv4<T extends N2oBasePage> implements NamespaceIO<T> {
    private Namespace regionDefaultNamespace = RegionIOv3.NAMESPACE;
    private Namespace pageDefaultNamespace = PageIOv4.NAMESPACE;
    private Namespace actionDefaultNamespace = ActionIOv2.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "html-title", m::getHtmlTitle, m::setHtmlTitle);
        p.attribute(e, "src", m::getSrc, m::setSrc);
        p.attribute(e, "class", m::getCssClass, m::setCssClass);
        p.attribute(e, "style", m::getStyle, m::setStyle);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "modal-size", m::getModalSize, m::setModalSize);
        p.attributeBoolean(e, "show-title", m::getShowTitle, m::setShowTitle);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionsBar::new, this::action);
        p.childAttributeEnum(e, "actions", "generate", m::getActionGenerate, m::setActionGenerate, GenerateType.class);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIOv2());
        p.children(e, "datasources", "datasource", m::getDatasources, m::setDatasources, N2oDatasource::new, this::datasource);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    private void action(Element e, ActionsBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "name", a::getLabel, a::setLabel);
        p.attribute(e, "datasource", a::getDatasource, a::setDatasource);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attribute(e, "icon", a::getIcon, a::setIcon);
        p.attribute(e, "visible", a::getVisible, a::setVisible);
        p.attribute(e, "enabled", a::getEnabled, a::setEnabled);
        p.anyChild(e, null, a::getAction, a::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    private void datasource(Element e, N2oDatasource a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "query-id", a::getQueryId, a::setQueryId);
        p.attribute(e, "object-id", a::getObjectId, a::setObjectId);
        p.attributeEnum(e, "default-values-mode", a::getDefaultValuesMode, a::setDefaultValuesMode, DefaultValuesMode.class);
        p.attribute(e, "route", a::getRoute, a::setRoute);
        p.attributeInteger(e, "size", a::getSize, a::setSize);
        p.child(e, null, "submit", a::getSubmit, a::setSubmit, Submit::new, this::submit);
        p.anyChildren(e, "dependencies", a::getDependencies, a::setDependencies,
                p.oneOf(N2oDatasource.Dependency.class)
                        .add("fetch", N2oDatasource.FetchDependency.class, this::fetch));
        p.childrenByEnum(e, "filters", a::getFilters, a::setFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterType.class, this::filters);
    }

    private void filters(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "param", pf::getParam, pf::setParam);
        p.attributeBoolean(e, "routable", pf::getRoutable, pf::setRoutable);
        p.attribute(e, "value", pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attribute(e, "datasource", pf::getDatasource, pf::setDatasource);
        p.attributeEnum(e, "model", pf::getModel, pf::setModel, ReduxModel.class);
        p.attributeBoolean(e, "required", pf::getRequired, pf::setRequired);
        p.childrenToStringArray(e, null, "value", pf::getValueList, pf::setValueList);
    }

    private void fetch(Element e, N2oDatasource.FetchDependency t, IOProcessor p) {
        p.attributeArray(e, "on", ",", t::getOn, t::setOn);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

    private void submit(Element e, Submit t, IOProcessor p) {
        p.attribute(e, "operation-id", t::getOperationId, t::setOperationId);
        p.attributeBoolean(e, "message-on-success", t::getMessageOnSuccess, t::setMessageOnSuccess);
        p.attributeBoolean(e, "message-on-fail", t::getMessageOnFail, t::setMessageOnFail);
        p.attributeEnum(e, "message-position", t::getMessagePosition, t::setMessagePosition, MessagePosition.class);
        p.attributeEnum(e, "message-placement", t::getMessagePlacement, t::setMessagePlacement, MessagePlacement.class);
        p.attribute(e, "message-widget-id", t::getMessageWidgetId, t::setMessageWidgetId);
        p.attributeBoolean(e, "refresh-on-success", t::getRefreshOnSuccess, t::setRefreshOnSuccess);
        p.attributeArray(e, "refresh-datasources", ",", t::getRefreshDatasources, t::setRefreshDatasources);
        p.attribute(e, "route", t::getRoute, t::setRoute);
        p.attributeBoolean(e, "submit-all", t::getSubmitAll, t::setSubmitAll);
        p.attributeEnum(e, "auto-submit-on", t::getSubmitOn, t::setSubmitOn, SubmitOn.class);
        p.children(e, null, "path-param", t::getPathParams, t::setPathParams, N2oParam.class, this::submitParam);
        p.children(e, null, "header-param", t::getHeaderParams, t::setHeaderParams, N2oParam.class, this::submitParam);
        p.children(e, null, "form-param", t::getFormParams, t::setFormParams, N2oFormParam.class, this::submitFormParam);
    }

    private void submitParam(Element e, N2oParam t, IOProcessor p) {
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "value", t::getValue, t::setValue);
        p.attribute(e, "datasource", t::getDatasource, t::setDatasource);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
    }

    private void submitFormParam(Element e, N2oFormParam t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        if (t.getId() == null)
            p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "value", t::getValue, t::setValue);
        p.attribute(e, "datasource", t::getDatasource, t::setDatasource);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
    }

    @Override
    public String getNamespaceUri() {
        return pageDefaultNamespace.getURI();
    }

    public Namespace getRegionDefaultNamespace() {
        return regionDefaultNamespace;
    }

    public void setRegionDefaultNamespace(String regionDefaultNamespace) {
        this.regionDefaultNamespace = Namespace.getNamespace(regionDefaultNamespace);
    }
}
