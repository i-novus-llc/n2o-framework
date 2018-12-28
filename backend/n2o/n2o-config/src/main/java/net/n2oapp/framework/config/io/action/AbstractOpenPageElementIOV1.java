package net.n2oapp.framework.config.io.action;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;

/**
 * Абстрактная реализация чтения/записи действия открытия старницы или модального окна
 */
public abstract class AbstractOpenPageElementIOV1<T extends N2oAbstractPageAction> extends AbstractActionElementIOV1<T> {
    @Override
    public void io(Element e, T op, IOProcessor p) {
        super.io(e, op, p);
        p.attribute(e, "page-id", op::getPageId, op::setPageId);
        p.attribute(e, "page-name", op::getPageName, op::setPageName);
        p.attribute(e, "submit-operation-id", op::getSubmitOperationId, op::setSubmitOperationId);
        p.attribute(e, "submit-label", op::getSubmitLabel, op::setSubmitLabel);
        p.attributeEnum(e, "submit-model", op::getSubmitModel, op::setSubmitModel, ReduxModel.class);
        p.attribute(e, "object-id", op::getObjectId, op::setObjectId);
        p.attributeEnum(e, "upload", op::getUpload, op::setUpload, UploadType.class);
        p.attribute(e, "master-field-id", op::getMasterFieldId, op::setMasterFieldId);
        p.attribute(e, "detail-field-id", op::getDetailFieldId, op::setDetailFieldId);
        p.attributeBoolean(e, "focus-after-submit", op::getFocusAfterSubmit, op::setFocusAfterSubmit);
        p.attributeBoolean(e, "close-after-submit", op::getCloseAfterSubmit, op::setCloseAfterSubmit);
        p.attribute(e, "redirect-url-after-submit", op::getRedirectUrlAfterSubmit, op::setRedirectUrlAfterSubmit);
        p.attributeEnum(e, "redirect-target-after-submit", op::getRedirectTargetAfterSubmit, op::setRedirectTargetAfterSubmit, Target.class);
        p.attributeBoolean(e, "refresh-after-submit", op::getRefreshAfterSubmit, op::setRefreshAfterSubmit);
        p.attributeBoolean(e, "refresh-on-close", op::getRefreshOnClose, op::setRefreshOnClose);
        p.attribute(e,"route", op::getRoute,op::setRoute);
        p.childrenByEnum(e, "pre-filters", op::getPreFilters, op::setPreFilters, N2oPreFilter::getType,
                N2oPreFilter::setType, N2oPreFilter::new, FilterType.class, this::prefilter);
        p.childrenByEnum(e, "pre-fields", op::getPreFields, op::setPreFields, N2oPreField::getType,
                N2oPreField::setType, N2oPreField::new, FilterType.class, this::prefield);
    }

    private void prefilter(Element e, N2oPreFilter pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "value", pf::getValueAttr, pf::setValueAttr);
        p.attribute(e, "values", pf::getValuesAttr, pf::setValuesAttr);
        p.attribute(e, "ref-widget-id", pf::getRefWidgetId, pf::setRefWidgetId);
        p.attributeEnum(e, "ref-model", pf::getRefModel, pf::setRefModel, ReduxModel.class);
        p.childrenToStringArray(e, null, "value", pf::getValues, pf::setValues);
    }

    private void prefield(Element e, N2oPreField pf, IOProcessor p) {
        p.attribute(e, "field-id", pf::getFieldId, pf::setFieldId);
        p.attribute(e, "value", pf::getValue, pf::setValue);
        p.attributeArray(e, "values", ",", pf::getValues, pf::setValues);
    }

}
