package net.n2oapp.framework.api.metadata.io;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Фабрика чтения / записи элементов основанная на неймспейсах
 */
public interface NamespaceIOFactory<T extends NamespaceUriAware, R extends NamespaceReader<? extends T>, P extends NamespacePersister<? super T>>
        extends NamespacePersisterFactory<T,P>, NamespaceReaderFactory<T,R> {

    /**
     * Базовый класс элементов фабрики
     */
    Class<T> getBaseElementClass();

    /**
     * Добавить типизированный по неймспейсу ридер / персистер
     * @param nio Типизированный по неймспейсу ридер / персистер
     * @return Фабрика
     */
    NamespaceIOFactory<T, R, P> add(NamespaceIO<? extends T> nio);

    /**
     * Добавить типизированный ридер / персистер
     * @param namespace Неймспейс
     * @param tio Типизированный ридер / персистер
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
            }

            @Override
            public void io(Element e, T m, IOProcessor p) {
                tio.io(e,m,p);
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
}
