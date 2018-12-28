package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import org.jdom.Namespace;

/**
 * Интерфейс  поля ввода версии 1
 */
public interface ControlIOv1 extends NamespaceUriAware, BaseElementClassAware<N2oField> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-control-1.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oField> getBaseElementClass() {
        return N2oField.class;
    }
}
