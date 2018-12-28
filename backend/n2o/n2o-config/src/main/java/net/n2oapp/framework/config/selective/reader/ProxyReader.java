package net.n2oapp.framework.config.selective.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author V. Alexeev.
 * @date 04.03.2016
 */
public class ProxyReader<T extends NamespaceUriAware> extends AbstractFactoredReader {

    public ProxyReader() {
    }

    public ProxyReader(NamespaceReaderFactory readerFactory) {
        setReaderFactory(readerFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T read(Element element, Namespace namespace) {
        return (T) readerFactory.produce(element).read(element);
    }

    @Override
    public Class getElementClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNamespaceUri() {
        return "proxy";
    }

    @Override
    public String getElementName() {
        return "proxy";
    }
}
