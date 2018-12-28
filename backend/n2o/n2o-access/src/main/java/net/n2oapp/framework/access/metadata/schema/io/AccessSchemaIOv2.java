package net.n2oapp.framework.access.metadata.schema.io;

import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import org.jdom.Namespace;

/**
 * Интерфейс IO для схемы доступа AccessSchema v2.0
 */

public interface AccessSchemaIOv2 extends NamespaceUriAware, BaseElementClassAware<N2oAccessSchema> {

    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/access-2.0");

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
    default Class<N2oAccessSchema> getBaseElementClass() {
        return N2oAccessSchema.class;
    }
}
