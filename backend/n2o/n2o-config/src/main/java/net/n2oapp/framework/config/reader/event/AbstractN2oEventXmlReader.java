package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;

/**
 * Абстрактная реализация считывания эвента
 */
public abstract class AbstractN2oEventXmlReader<E extends N2oAction> implements N2oEventXmlReader<E> {

    protected NamespaceReaderFactory readerFactory;
    private String namespaceUri = "http://n2oapp.net/framework/config/schema/n2o-event-1.0";

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }
}
