package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Проксирующий считыватель элементов по неймспейсу
 * @param <T> Тип модели элемента
 */
public class ProxyNamespaceIO<T extends NamespaceUriAware> implements NamespaceReader<T>, NamespacePersister<T>, IOProcessorAware {

    private NamespaceIO<T> io;
    private IOProcessor processor;

    public ProxyNamespaceIO(NamespaceIO<T> io, IOProcessor processor) {
        this.io = io;
        this.processor = processor;
    }

    public ProxyNamespaceIO(NamespaceIO<T> io) {
        this.io = io;
    }

    @Override
    public Class<T> getElementClass() {
        return io.getElementClass();
    }

    @Override
    public String getElementName() {
        return io.getElementName();
    }

    @Override
    public String getNamespaceUri() {
        return io.getNamespaceUri();
    }


    @Override
    public Element persist(T entity, Namespace namespace) {
        Element element = new Element(getElementName(), entity.getNamespaceUri() != null ? entity.getNamespaceUri() : namespace.getURI());
        io.io(element, entity, processor);
        return element;
    }

    @Override
    public T read(Element element) {
        T entity = newInstance(element);
        io.io(element, entity, processor);
        entity.setNamespaceUri(element.getNamespaceURI());
        entity.setNamespacePrefix(element.getNamespacePrefix());
        return entity;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
    }

    @Override
    public void setIOProcessor(IOProcessor processor) {
        this.processor = processor;
    }
}
