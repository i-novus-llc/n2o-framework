package net.n2oapp.framework.api.metadata.io;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Проксирующий считыватель типизированных элементов
 * @param <T> Тип модели элемента
 */
public class ProxyTypedElementIO<T> implements TypedElementReader<T>, TypedElementPersister<T>, IOProcessorAware {
    @Getter
    private TypedElementIO<T> io;
    @Getter
    private IOProcessor processor;

    public ProxyTypedElementIO(TypedElementIO<T> io, IOProcessor processor) {
        this.io = io;
        this.processor = processor;
    }

    public ProxyTypedElementIO(TypedElementIO<T> io) {
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
    public void setIOProcessor(IOProcessor processor) {
        this.processor = processor;
    }

    @Override
    public Element persist(T entity, Namespace namespace) {
        Element element = new Element(getElementName(), namespace.getURI());
        io.io(element, entity, processor);
        return element;
    }

    @Override
    public T read(Element element) {
        T entity = newInstance(element);
        io.io(element, entity, processor);
        return entity;
    }
}
