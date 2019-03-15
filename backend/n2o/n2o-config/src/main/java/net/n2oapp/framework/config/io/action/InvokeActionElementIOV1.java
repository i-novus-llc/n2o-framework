package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись выполнения действия объекта
 */
@Component
public class InvokeActionElementIOV1 extends AbstractActionElementIOV1<N2oInvokeAction> {
    @Override
    public void io(Element e, N2oInvokeAction ia, IOProcessor p) {
        super.io(e, ia, p);
        p.attribute(e, "operation-id", ia::getOperationId, ia::setOperationId);
        p.attribute(e,"route", ia::getRoute, ia::setRoute);
        p.attributeBoolean(e, "close-after-success", ia::getCloseAfterSuccess, ia::setCloseAfterSuccess);
        p.attribute(e,"refresh-widget-id", ia::getRefreshWidgetId, ia::setRefreshWidgetId);
        p.attributeBoolean(e, "refresh-on-success", ia::getRefreshOnSuccess, ia::setRefreshOnSuccess);
        p.attribute(e, "redirect-url", ia::getRedirectUrl, ia::setRedirectUrl);
        p.attributeEnum(e, "redirect-target", ia::getRedirectTarget, ia::setRedirectTarget, Target.class);
    }

    @Override
    public String getElementName() {
        return "invoke";
    }

    @Override
    public Class<N2oInvokeAction> getElementClass() {
        return N2oInvokeAction.class;
    }
}