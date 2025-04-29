package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.widget.FormModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePositionEnum;
import net.n2oapp.framework.config.io.control.v2.ControlIOv2;
import net.n2oapp.framework.config.io.fieldset.v4.FieldsetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись виджета Форма
 */
@Component
public class FormElementIOV4 extends WidgetElementIOv4<N2oForm> {

    @Override
    public void io(Element e, N2oForm f, IOProcessor p) {
        super.io(e, f, p);
        p.attributeBoolean(e, "unsaved-data-prompt", f::getUnsavedDataPrompt, f::setUnsavedDataPrompt);
        p.attributeEnum(e, "mode", f::getMode, f::setMode, FormModeEnum.class);
        p.attribute(e, "default-values-query-id", f::getDefaultValuesQueryId, f::setDefaultValuesQueryId);
        p.anyChildren(e, "fields", f::getItems, f::setItems, p.anyOf(SourceComponent.class), FieldsetIOv4.NAMESPACE, ControlIOv2.NAMESPACE);
        p.child(e, null, "submit", f::getSubmit, f::setSubmit, Submit.class, this::submit);
        f.adapterV4();
    }

    private void submit(Element e, Submit t, IOProcessor p) {
        p.attribute(e, "operation-id", t::getOperationId, t::setOperationId);
        p.attributeBoolean(e, "message-on-success", t::getMessageOnSuccess, t::setMessageOnSuccess);
        p.attributeBoolean(e, "message-on-fail", t::getMessageOnFail, t::setMessageOnFail);
        p.attributeEnum(e, "message-position", t::getMessagePosition, t::setMessagePosition, MessagePositionEnum.class);
        p.attributeEnum(e, "message-placement", t::getMessagePlacement, t::setMessagePlacement, MessagePlacementEnum.class);
        p.attributeBoolean(e, "refresh-on-success", t::getRefreshOnSuccess, t::setRefreshOnSuccess);
        p.attribute(e, "refresh-widget-id", t::getRefreshWidgetId, t::setRefreshWidgetId);
        p.attribute(e, "route", t::getRoute, t::setRoute);
        p.children(e, null, "path-param", t::getPathParams, t::setPathParams, N2oParam.class, this::submitParam);
        p.children(e, null, "header-param", t::getHeaderParams, t::setHeaderParams, N2oParam.class, this::submitParam);
        p.children(e, null, "form-param", t::getFormParams, t::setFormParams, N2oFormParam.class, this::submitFormParam);
    }

    private void submitParam(Element e, N2oParam t, IOProcessor p) {
        p.attribute(e, "name", t::getName, t::setName);
        p.attribute(e, "value", t::getValue, t::setValue);
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getModel, t::setModel, ReduxModelEnum.class);
    }

    private void submitFormParam(Element e, N2oFormParam t, IOProcessor p) {
        p.attribute(e, "id", t::getName, t::setName);
        p.attribute(e, "value", t::getValue, t::setValue);
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getModel, t::setModel, ReduxModelEnum.class);
    }

    @Override
    public String getElementName() {
        return "form";
    }

    @Override
    public Class<N2oForm> getElementClass() {
        return N2oForm.class;
    }
}
