package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

public abstract class AbstractNamespaceIO<T extends NamespaceUriAware> extends AbstractTypedElementIO<T>
        implements NamespaceIO<T>, IOProcessorAware  {
    private String namespaceUri;

    public AbstractNamespaceIO() {
    }

    public AbstractNamespaceIO(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }
}
