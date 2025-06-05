package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;

/**
 * Абстрактная реализация чтения/записи действия открытия старницы или модального окна
 */
public abstract class AbstractOpenPageElementIOV1<T extends N2oAbstractPageAction> extends AbstractActionElementIOV1<T> {
    private static final String VALUE = "value";
    @Override
    public void io(Element e, T op, IOProcessor p) {
        super.io(e, op, p);
        p.attribute(e, "page-id", op::getPageId, op::setPageId);
        p.attribute(e, "page-name", op::getPageName, op::setPageName);
        p.attributeEnum(e, "target", op::getTarget, op::setTarget, TargetEnum.class);
        p.attribute(e, "submit-operation-id", op::getSubmitOperationId, op::setSubmitOperationId);
        p.attribute(e, "submit-label", op::getSubmitLabel, op::setSubmitLabel);
        p.attributeEnum(e, "submit-model", op::getSubmitModel, op::setSubmitModel, ReduxModelEnum.class);
        p.attribute(e, "object-id", op::getObjectId, op::setObjectId);
        p.attributeEnum(e, "upload", op::getMode, op::setMode, DefaultValuesModeEnum.class);
        p.attribute(e, "master-field-id", op::getMasterFieldId, op::setMasterFieldId);
        p.attribute(e, "detail-field-id", op::getDetailFieldId, op::setDetailFieldId);
        p.attribute(e, "master-param", op::getMasterParam, op::setMasterParam);
        p.attributeBoolean(e, "close-after-submit", op::getCloseAfterSubmit, op::setCloseAfterSubmit);
        p.attribute(e, "redirect-url-after-submit", op::getRedirectUrlAfterSubmit, op::setRedirectUrlAfterSubmit);
        p.attributeEnum(e, "redirect-target-after-submit", op::getRedirectTargetAfterSubmit, op::setRedirectTargetAfterSubmit, TargetEnum.class);
        p.attributeBoolean(e, "refresh-after-submit", op::getRefreshAfterSubmit, op::setRefreshAfterSubmit);
        p.attributeBoolean(e, "refresh-on-close", op::getRefreshOnClose, op::setRefreshOnClose);
        p.attributeBoolean(e, "unsaved-data-prompt-on-close", op::getUnsavedDataPromptOnClose, op::setUnsavedDataPromptOnClose);
        p.attribute(e, "route", op::getRoute, op::setRoute);
        p.childrenByEnum(e, "pre-filters", op::getPreFilters, op::setPreFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterTypeEnum.class, this::prefilter);
        p.children(e, null, "query-param", op::getQueryParams, op::addQueryParams, N2oQueryParam.class, this::param);
        p.children(e, null, "path-param", op::getPathParams, op::addPathParams, N2oPathParam.class, this::param);
        op.adaptV1();
    }

    private void prefilter(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, VALUE, pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "param", pf::getParam, pf::setParam);
        p.attributeBoolean(e, "routable", pf::getRoutable, pf::setRoutable);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attribute(e, "ref-widget-id", pf::getRefWidgetId, pf::setRefWidgetId);
        p.attributeEnum(e, "ref-model", pf::getModel, pf::setModel, ReduxModelEnum.class);
        p.childrenToStringArray(e, null, VALUE, pf::getValueList, pf::setValueList);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, VALUE, param::getValue, param::setValue);
        p.attribute(e, "ref-widget-id", param::getRefWidgetId, param::setRefWidgetId);
        p.attributeEnum(e, "ref-model", param::getModel, param::setModel, ReduxModelEnum.class);
    }

}
