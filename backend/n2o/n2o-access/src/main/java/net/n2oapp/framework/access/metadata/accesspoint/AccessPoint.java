package net.n2oapp.framework.access.metadata.accesspoint;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.copy;

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
