package net.n2oapp.framework.access.metadata.accesspoint;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Точки доступа
 */
public abstract class AccessPoint implements Source, Compiled, NamespaceUriAware {

    private String namespaceUri;

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("please implement equals() and hashCode()");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("please implement equals() and hashCode()");
    }


}
