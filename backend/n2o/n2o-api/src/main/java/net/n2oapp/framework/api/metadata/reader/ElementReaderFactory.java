package net.n2oapp.framework.api.metadata.reader;

import org.jdom.Element;
import org.jdom.Namespace;


/**
 * Фабрика создания сервисов для чтения метаданных из xml файлов в объекты n2o
 */
public interface ElementReaderFactory<T, R extends TypedElementReader<? extends T>> {

    R produce(Element element);

}
