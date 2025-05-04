package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Фабрика чтения / записи элементов основанная на неймспейсах
 */
public interface NamespaceIOFactory<T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends NamespacePersister<? super T>>
        extends NamespacePersisterFactory<T, P>, NamespaceReaderFactory<T, R> {

    /**
     * Базовый класс элементов фабрики
     */
    Class<T> getBaseElementClass();

    /**
     * Добавить типизированный по неймспейсу ридер / персистер
     *
     * @param nio Типизированный по неймспейсу ридер / персистер
     * @return Фабрика
     */
    NamespaceIOFactory<T, R, P> add(NamespaceIO<? extends T> nio);

    /**
     * Игнорировать элементы
     *
     * @param elementNames Список элементов
     * @return Фабрика
     */
    NamespaceIOFactory<T, R, P> ignore(String... elementNames);

    /**
     * Добавить типизированный ридер / персистер
     *
     * @param namespace Неймспейс
     * @param tio       Типизированный ридер / персистер
     * @return Фабрика
     */
    default NamespaceIOFactory<T, R, P> add(Namespace namespace, TypedElementIO<T> tio) {
        return add(new NamespaceIO<T>() {

            @Override
            public String getNamespaceUri() {
                return namespace.getURI();
            }

            @Override
            public void setNamespaceUri(String namespaceUri) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void io(Element e, T m, IOProcessor p) {
                tio.io(e, m, p);
            }

            @Override
            public String getElementName() {
                return tio.getElementName();
            }

            @Override
            public Class<T> getElementClass() {
                return tio.getElementClass();
            }
        });
    }

    /**
     * Добавить ридер / персистер DOM элементов
     *
     * @param elementName  - имя элемента
     * @param namespaceURI - Неймспейс
     * @param elementClass - класс элемента
     * @param tio          - ридер / персистер элемента
     * @param <E>          - extends NamespaceUriAware
     * @return Фабрика
     */
    default <E extends T> NamespaceIOFactory<T, R, P> add(String elementName, String namespaceURI, Class<E> elementClass, ElementIO<E> tio) {
        return add(new NamespaceIO<E>() {
            @Override
            public Class<E> getElementClass() {
                return elementClass;
            }

            @Override
            public String getElementName() {
                return elementName;
            }

            @Override
            public String getNamespaceUri() {
                return namespaceURI;
            }

            @Override
            public void io(Element e, E m, IOProcessor p) {
                tio.io(e, m, p);
            }
        });
    }
}
