package net.n2oapp.framework.api.metadata.persister;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.PersisterFactoryAware;

/**
 * Абстрактная реализация записи метаданных в DOM элементы
 */
public abstract class AbstractN2oMetadataPersister<T extends NamespaceUriAware>
        implements NamespacePersister<T>, PersisterFactoryAware<T,NamespacePersister<T>> {
    protected String namespaceUri;
    protected String namespacePrefix;
    protected NamespacePersisterFactory persisterFactory;

    protected AbstractN2oMetadataPersister() {
    }

    public AbstractN2oMetadataPersister(String namespaceUri, String namespacePrefix) {
        this.namespaceUri = namespaceUri;
        this.namespacePrefix = namespacePrefix;
    }

    public abstract void setNamespaceUri(String uri);

    public abstract void setNamespacePrefix(String prefix);

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    @Override
    public void setPersisterFactory(NamespacePersisterFactory<T, NamespacePersister<T>> persisterFactory) {
        this.persisterFactory = persisterFactory;
    }
}
