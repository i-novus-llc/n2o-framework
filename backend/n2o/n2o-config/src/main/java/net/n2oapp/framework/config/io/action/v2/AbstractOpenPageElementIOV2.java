package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;

/**
 * Абстрактная реализация чтения/записи действия открытия страницы или модального окна версии 2.0
 */
public abstract class AbstractOpenPageElementIOV2<T extends N2oAbstractPageAction> extends AbstractActionElementIOV2<T> {
    @Override
    public void io(Element e, T op, IOProcessor p) {
        super.io(e, op, p);
        p.attribute(e, "page-id", op::getPageId, op::setPageId);
        p.attribute(e, "page-name", op::getPageName, op::setPageName);
        p.attribute(e, "submit-operation-id", op::getSubmitOperationId, op::setSubmitOperationId);
        p.attribute(e, "submit-label", op::getSubmitLabel, op::setSubmitLabel);
        p.attributeEnum(e, "submit-model", op::getSubmitModel, op::setSubmitModel, ReduxModel.class);
        p.attribute(e, "datasource", op::getDatasource, op::setDatasource);
        p.attributeBoolean(e, "close-after-submit", op::getCloseAfterSubmit, op::setCloseAfterSubmit);
        p.attribute(e, "redirect-url-after-submit", op::getRedirectUrlAfterSubmit, op::setRedirectUrlAfterSubmit);
        p.attributeEnum(e, "redirect-target-after-submit", op::getRedirectTargetAfterSubmit, op::setRedirectTargetAfterSubmit, Target.class);
        p.attributeBoolean(e, "refresh-after-submit", op::getRefreshAfterSubmit, op::setRefreshAfterSubmit);
        p.attributeBoolean(e, "refresh-on-close", op::getRefreshOnClose, op::setRefreshOnClose);
        p.attributeBoolean(e, "unsaved-data-prompt-on-close", op::getUnsavedDataPromptOnClose, op::setUnsavedDataPromptOnClose);
        p.attribute(e, "route", op::getRoute, op::setRoute);
        p.children(e, "datasources", "datasource", op::getDatasources,op::setDatasources, N2oDatasource::new, this::datasource);
        p.anyChildren(e, "params", op::getParams, op::setParams,
                p.oneOf(N2oParam.class)
                        .add("path-param", N2oPathParam.class, this::param)
                        .add("query-param", N2oQueryParam.class, this::param));
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attribute(e, "datasource", param::getDatasource, param::setDatasource);
        p.attributeEnum(e, "model", param::getModel, param::setModel, ReduxModel.class);
    }

    private void datasource(Element e, N2oDatasource a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "query-id", a::getQueryId, a::setQueryId);
        p.attribute(e, "object-id", a::getObjectId, a::setObjectId);
        p.attributeEnum(e, "default-values-mode", a::getDefaultValuesMode, a::setDefaultValuesMode, DefaultValuesMode.class);
        p.attributeInteger(e, "size", a::getSize, a::setSize);
    }

}
