package net.n2oapp.framework.access.metadata.accesspoint.io;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import org.jdom.Namespace;

/**
 * Интерфейс IO для Точек доступа v2.0
 */
public interface AccessPointIOv2 extends NamespaceUriAware, BaseElementClassAware<AccessPoint> {

    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/access-point-2.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default void setNamespaceUri(String namespaceUri) {}

    @Override
    default Class<AccessPoint> getBaseElementClass() {
        return AccessPoint.class;
    }
}
