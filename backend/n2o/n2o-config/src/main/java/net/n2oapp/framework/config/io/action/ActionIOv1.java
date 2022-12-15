package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.action.N2oAbstractAction;
import org.jdom2.Namespace;

/**
 *  Интерфейс  обработчика действий версии 1
 */
public interface ActionIOv1 extends NamespaceUriAware, BaseElementClassAware<N2oAbstractAction> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/action-1.0");

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
