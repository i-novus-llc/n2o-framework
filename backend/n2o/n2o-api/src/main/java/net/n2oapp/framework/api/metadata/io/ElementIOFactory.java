package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.persister.ElementPersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;

/**
 * Фабрика чтения / записи элементов
 */
public interface ElementIOFactory<T, R extends TypedElementReader<? extends T>, P extends TypedElementPersister<? super T>>
        extends ElementReaderFactory<T, R>, ElementPersisterFactory<T, P> {

    /**
     * Базовый класс элементов фабрики
     */
    Class<T> getBaseElementClass();

    ElementIOFactory<T,R,P> add(TypedElementIO<? extends T> tio);

    default <E extends T> ElementIOFactory<T,R,P> add(String elementName, Class<E> elementClass, ElementIO<E> io) {
        return add(new TypedElementIO<E>() {
            @Override
            public Class<E> getElementClass() {
                return elementClass;
            }

            @Override
            public void io(Element e, E t, IOProcessor p) {
                io.io(e,t,p);
            }

            @Override
            public String getElementName() {
                return elementName;
            }
        });
    }
}
