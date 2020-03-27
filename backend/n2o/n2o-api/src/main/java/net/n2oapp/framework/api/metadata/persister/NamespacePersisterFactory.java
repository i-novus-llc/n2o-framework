package net.n2oapp.framework.api.metadata.persister;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import org.jdom.Namespace;

/**
 * Фабрика персистеров элементов поражденных по неймспейсу
 *
 * @param <T> Тип модели
 * @param <P> Персистер
 */
public interface NamespacePersisterFactory<T extends NamespaceUriAware, P extends NamespacePersister<? super T>> extends ElementPersisterFactory<T, P> {

    /**
     * Произвести персистер по неймспейсу и классу модели
     *
     * @param namespaces Неймспейсы
     * @param clazz      Класс модели
     * @return Персистер
     */
    P produce(Class<T> clazz, Namespace... namespaces);

    @Override
    @SuppressWarnings("unchecked")
    default P produce(T entity) {
        return produce((Class<T>) entity.getClass(), entity.getNamespace());
    }

    void add(NamespacePersister<T> persister);
}
