package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oCachedDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePositionEnum;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кэширующего источника данных
 */
@Component
public class CachedDatasourceIO extends BaseDatasourceIO<N2oCachedDatasource> {
    private static final String VALUE = "value";
    private static final String DATASOURCE = "datasource";
    private static final String MODEL = "model";

    @Override
    public void io(Element e, N2oCachedDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "query-id", ds::getQueryId, ds::setQueryId);
        p.attribute(e, "object-id", ds::getObjectId, ds::setObjectId);
        p.attribute(e, "route", ds::getRoute, ds::setRoute);
        p.attributeInteger(e, "size", ds::getSize, ds::setSize);
        p.attribute(e, "storage-key", ds::getStorageKey, ds::setStorageKey);
        p.attributeEnum(e, "storage-type", ds::getStorageType, ds::setStorageType, BrowserStorageTypeEnum.class);
        p.attribute(e, "cache-expires", ds::getCacheExpires, ds::setCacheExpires);
        p.attributeBoolean(e, "fetch-on-init", ds::getFetchOnInit, ds::setFetchOnInit);
        p.child(e, null, "submit", ds::getSubmit, ds::setSubmit, Submit::new, this::submit);
        p.childrenByEnum(e, "filters", ds::getFilters, ds::setFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterTypeEnum.class, this::filters);
    }

    @Override
    protected void filters(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "param", pf::getParam, pf::setParam);
        p.attributeBoolean(e, "required", pf::getRequired, pf::setRequired);
        p.attribute(e, VALUE, pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attribute(e, DATASOURCE, pf::getDatasourceId, pf::setDatasourceId);
        p.attributeEnum(e, MODEL, pf::getModel, pf::setModel, ReduxModelEnum.class);
        p.childrenToStringArray(e, null, VALUE, pf::getValueList, pf::setValueList);
    }

    private void submit(Element e, Submit t, IOProcessor p) {
        p.attributeBoolean(e, "clear-cache-after-submit", t::getClearCacheAfterSubmit, t::setClearCacheAfterSubmit);
        p.attribute(e, "operation-id", t::getOperationId, t::setOperationId);
        p.attributeBoolean(e, "message-on-success", t::getMessageOnSuccess, t::setMessageOnSuccess);
        p.attributeBoolean(e, "message-on-fail", t::getMessageOnFail, t::setMessageOnFail);
        p.attributeEnum(e, "message-position", t::getMessagePosition, t::setMessagePosition, MessagePositionEnum.class);
        p.attributeEnum(e, "message-placement", t::getMessagePlacement, t::setMessagePlacement, MessagePlacementEnum.class);
        p.attribute(e, "message-widget-id", t::getMessageWidgetId, t::setMessageWidgetId);
        p.attributeBoolean(e, "refresh-on-success", t::getRefreshOnSuccess, t::setRefreshOnSuccess);
        p.attributeArray(e, "refresh-datasources", ",", t::getRefreshDatasourceIds, t::setRefreshDatasourceIds);
        p.attribute(e, "route", t::getRoute, t::setRoute);
        p.attributeBoolean(e, "submit-all", t::getSubmitAll, t::setSubmitAll);
        p.children(e, null, "path-param", t::getPathParams, t::setPathParams, N2oParam.class, this::submitParam);
        p.children(e, null, "header-param", t::getHeaderParams, t::setHeaderParams, N2oParam.class, this::submitParam);
        p.children(e, null, "form-param", t::getFormParams, t::setFormParams, N2oFormParam.class, this::submitFormParam);
    }

    private void submitParam(Element e, N2oParam t, IOProcessor p) {
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, VALUE, t::getValue, t::setValue);
        p.attribute(e, DATASOURCE, t::getDatasourceId, t::setDatasourceId);
        p.attributeEnum(e, MODEL, t::getModel, t::setModel, ReduxModelEnum.class);
    }

    private void submitFormParam(Element e, N2oFormParam t, IOProcessor p) {
        p.attribute(e, "id", t::getName, t::setName);
        p.attribute(e, VALUE, t::getValue, t::setValue);
        p.attribute(e, DATASOURCE, t::getDatasourceId, t::setDatasourceId);
        p.attributeEnum(e, MODEL, t::getModel, t::setModel, ReduxModelEnum.class);
    }

    @Override
    public Class<N2oCachedDatasource> getElementClass() {
        return N2oCachedDatasource.class;
    }

    @Override
    public String getElementName() {
        return "cached-datasource";
    }
}
