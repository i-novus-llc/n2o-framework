package net.n2oapp.framework.api.metadata.aware;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jdom2.Namespace;

/**
 * Знание о URI схемы
 */
public interface NamespaceUriAware {
    /**
     * @return URI схемы
     */
    @JsonIgnore
    String getNamespaceUri();

    /**
     * @return Префикс схемы
     */
    @JsonIgnore
    default String getNamespacePrefix() {
        return "";
    }

    @JsonIgnore
    default Namespace getNamespace() {
        return Namespace.getNamespace(getNamespacePrefix(), getNamespaceUri());
    }

    default void setNamespaceUri(String namespaceUri) {}
}
