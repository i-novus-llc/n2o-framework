package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oQueryParam;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.datasource.AbstractDatasourceIO;
import net.n2oapp.framework.config.io.toolbar.v2.ToolbarIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Абстрактная реализация чтения/записи действия открытия страницы или модального окна версии 2.0
 */
public abstract class AbstractOpenPageElementIOV2<T extends N2oAbstractPageAction> extends AbstractActionElementIOV2<T> {
    private static final Namespace actionDefaultNamespace = ActionIOv2.NAMESPACE;
    private static final Namespace datasourceDefaultNamespace = AbstractDatasourceIO.NAMESPACE;

    @Override
    public void io(Element e, T op, IOProcessor p) {
        super.io(e, op, p);
        p.attribute(e, "page-id", op::getPageId, op::setPageId);
        p.attribute(e, "page-name", op::getPageName, op::setPageName);
        p.attribute(e, "submit-operation-id", op::getSubmitOperationId, op::setSubmitOperationId);
        p.attribute(e, "submit-label", op::getSubmitLabel, op::setSubmitLabel);
        p.attributeEnum(e, "submit-model", op::getSubmitModel, op::setSubmitModel, ReduxModel.class);
        p.attributeEnum(e, "target", op::getTarget, op::setTarget, Target.class);
        p.attribute(e, "object-id", op::getObjectId, op::setObjectId);
        p.attributeBoolean(e, "close-after-submit", op::getCloseAfterSubmit, op::setCloseAfterSubmit);
        p.attribute(e, "redirect-url-after-submit", op::getRedirectUrlAfterSubmit, op::setRedirectUrlAfterSubmit);
        p.attributeEnum(e, "redirect-target-after-submit", op::getRedirectTargetAfterSubmit, op::setRedirectTargetAfterSubmit, Target.class);
        p.attributeBoolean(e, "refresh-after-submit", op::getRefreshAfterSubmit, op::setRefreshAfterSubmit);
        p.attributeBoolean(e, "refresh-on-close", op::getRefreshOnClose, op::setRefreshOnClose);
        p.attributeArray(e, "refresh-datasources", ",", op::getRefreshDatasourceIds, op::setRefreshDatasourceIds);
        p.attributeBoolean(e, "unsaved-data-prompt-on-close", op::getUnsavedDataPromptOnClose, op::setUnsavedDataPromptOnClose);
        p.attributeBoolean(e, "submit-message-on-success", op::getSubmitMessageOnSuccess, op::setSubmitMessageOnSuccess);
        p.attributeBoolean(e, "submit-message-on-fail", op::getSubmitMessageOnFail, op::setSubmitMessageOnFail);
        p.attribute(e, "route", op::getRoute, op::setRoute);
        p.children(e, "breadcrumbs", "crumb", op::getBreadcrumbs, op::setBreadcrumbs,
                N2oBreadcrumb.class, this::breadcrumbs);
        p.anyChildren(e, "datasources", op::getDatasources, op::setDatasources, p.anyOf(N2oAbstractDatasource.class), datasourceDefaultNamespace);
        p.anyChildren(e, "params", op::getParams, op::setParams,
                p.oneOf(N2oParam.class)
                        .add("path-param", N2oPathParam.class, this::param)
                        .add("query-param", N2oQueryParam.class, this::param));
        p.children(e, "toolbars", "toolbar", op::getToolbars, op::setToolbars, new ToolbarIOv2());
        p.children(e, "actions", "action",op::getActions, op::setActions, ActionBar::new, this::action);
    }

    private void breadcrumbs(Element e, N2oBreadcrumb c, IOProcessor p) {
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "path", c::getPath, c::setPath);
    }

    private void param(Element e, N2oParam param, IOProcessor p) {
        p.attribute(e, "name", param::getName, param::setName);
        p.attribute(e, "value", param::getValue, param::setValue);
        p.attribute(e, "datasource", param::getDatasourceId, param::setDatasourceId);
        p.attributeEnum(e, "model", param::getModel, param::setModel, ReduxModel.class);
    }

    private void action(Element e, ActionBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "name", a::getLabel, a::setLabel);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attribute(e, "icon", a::getIcon, a::setIcon);
        p.attribute(e, "visible", a::getVisible, a::setVisible);
        p.attribute(e, "enabled", a::getEnabled, a::setEnabled);
        p.anyChildren(e, null, a::getN2oActions, a::setN2oActions, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }
}
