package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.control.SubmitOn;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись источника данных
 */
public class DatasourceIO extends AbstractDatasourceIO<N2oDatasource> implements NamespaceUriAware {

    static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/datasource-1.0");

    @Override
    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    public Class<N2oDatasource> getElementClass() {
        return N2oDatasource.class;
    }

    @Override
    public String getElementName() {
        return "datasource";
    }

    @Override
    public void io(Element e, N2oDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "query-id", ds::getQueryId, ds::setQueryId);
        p.attribute(e, "object-id", ds::getObjectId, ds::setObjectId);
        p.attributeEnum(e, "default-values-mode", ds::getDefaultValuesMode, ds::setDefaultValuesMode, DefaultValuesMode.class);
        p.attribute(e, "route", ds::getRoute, ds::setRoute);
        p.attributeInteger(e, "size", ds::getSize, ds::setSize);
        p.child(e, null, "submit", ds::getSubmit, ds::setSubmit, Submit::new, this::submit);
        p.anyChildren(e, "dependencies", ds::getDependencies, ds::setDependencies,
                p.oneOf(N2oDatasource.Dependency.class)
                        .add("fetch", N2oDatasource.FetchDependency.class, this::fetch));
        p.childrenByEnum(e, "filters", ds::getFilters, ds::setFilters, N2oPreFilter::getType,
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
        p.attribute(e, "on", t::getOn, t::setOn);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
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

}
