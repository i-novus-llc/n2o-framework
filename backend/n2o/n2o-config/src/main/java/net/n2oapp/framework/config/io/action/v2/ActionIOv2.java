package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import org.jdom2.Namespace;

/**
 *  Интерфейс  обработчика действий версии 2.0
 */
public interface ActionIOv2 extends NamespaceUriAware, BaseElementClassAware<N2oAbstractAction> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/action-2.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oAbstractAction> getBaseElementClass() {
        return N2oAbstractAction.class;
    }
}
