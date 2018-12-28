package net.n2oapp.framework.api.metadata.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Абстрактная реализация чтения метаданных
 */
public abstract class AbstractFactoredReader<T extends NamespaceUriAware>
        implements NamespaceReader<T>, ReaderFactoryAware<T, NamespaceReader<T>> {

    protected String namespaceUri;
    protected NamespaceReaderFactory readerFactory;

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
        if (this.readerFactory == null)
            this.readerFactory = readerFactory;
    }

    @Override
    public T read(Element element) {
        return read(element, element.getNamespace());
    }

    /**
     * Чтение element в сущность
     * @param element элемент
     * @param namespace схема
     * @return сущность
     * @see ElementReader#read
     */
    @Deprecated
    public abstract T read(Element element, Namespace namespace);

}
