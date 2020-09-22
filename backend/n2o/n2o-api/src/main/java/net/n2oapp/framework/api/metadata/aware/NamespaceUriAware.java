package net.n2oapp.framework.api.metadata.aware;

import org.jdom2.Namespace;

/**
 * Знание о URI схемы
 */
public interface NamespaceUriAware {
    /**
     * @return URI схемы
     */
    String getNamespaceUri();

    /**
     * @return Префикс схемы
     */
    default String getNamespacePrefix() {
        return "";
    }

    default Namespace getNamespace() {
        return Namespace.getNamespace(getNamespacePrefix(), getNamespaceUri());
    }

    default void setNamespaceUri(String namespaceUri) {}
}
