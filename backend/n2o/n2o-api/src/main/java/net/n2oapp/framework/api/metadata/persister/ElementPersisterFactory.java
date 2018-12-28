package net.n2oapp.framework.api.metadata.persister;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Фабрика персистеров элементов
 */
public interface ElementPersisterFactory<T, P extends TypedElementPersister<? super T>> {

    /**
     * Произвести персистер по модели
     * @param t Модель
     * @return Персистер
     */
    P produce(T t);

}
