package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractMetaAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;

/**
 * Чтение\запись абстрактного действия, содержащего стандартные саги версии 2.0
 */
public abstract class AbstractMetaActionElementIOV2<T extends N2oAbstractMetaAction> extends AbstractActionElementIOV2<T> {
    @Override
    public void io(Element e, T a, IOProcessor p) {
        super.io(e, a, p);
        p.read(e, a, (el, md) -> {
            if (el.getAttribute("close-after-success") != null) {
                p.attributeBoolean(e, "close-after-success", a::getCloseOnSuccess, a::setCloseOnSuccess);
            }
        });
        p.attributeBoolean(e, "close-on-success", a::getCloseOnSuccess, a::setCloseOnSuccess);
        p.attributeBoolean(e, "double-close-on-success", a::getDoubleCloseOnSuccess, a::setDoubleCloseOnSuccess);
        p.attributeBoolean(e, "close-on-fail", a::getCloseOnFail, a::setCloseOnFail);
        p.attributeArray(e, "refresh-datasources", ",", a::getRefreshDatasourceIds, a::setRefreshDatasourceIds);
        p.attributeBoolean(e, "refresh-on-success", a::getRefreshOnSuccess, a::setRefreshOnSuccess);
        p.attribute(e, "redirect-url", a::getRedirectUrl, a::setRedirectUrl);
        p.attributeEnum(e, "redirect-target", a::getRedirectTarget, a::setRedirectTarget, Target.class);

    }
}
