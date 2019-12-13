package net.n2oapp.framework.api.metadata.persister;

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
