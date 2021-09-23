package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;

/**
 * Знание о фабрике ридеров
 */
public interface ReaderFactoryAware<T extends NamespaceUriAware, R extends NamespaceReader<? extends T>> {
    void setReaderFactory(NamespaceReaderFactory readerFactory);
}
