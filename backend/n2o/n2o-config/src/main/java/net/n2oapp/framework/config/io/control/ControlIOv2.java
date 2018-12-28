package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import org.jdom.Namespace;

/**
 * Интерфейс контролов версии 2
 */
public interface ControlIOv2 extends NamespaceUriAware, BaseElementClassAware<N2oField> {

    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/control-2.0");

    @Override
    default Class<N2oField> getBaseElementClass() {
        return N2oField.class;
    }

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default void setNamespaceUri(String namespaceUri) {

    }
}
