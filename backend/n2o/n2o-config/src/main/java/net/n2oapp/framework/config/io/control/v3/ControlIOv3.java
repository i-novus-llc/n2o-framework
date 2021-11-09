package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import org.jdom2.Namespace;

/**
 * Интерфейс контролов версии 3.0
 */
public interface ControlIOv3 extends NamespaceUriAware, BaseElementClassAware<N2oComponent> {

    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/control-3.0");

    @Override
    default Class<N2oComponent> getBaseElementClass() {
        return N2oComponent.class;
    }

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default void setNamespaceUri(String namespaceUri) {

    }
}
