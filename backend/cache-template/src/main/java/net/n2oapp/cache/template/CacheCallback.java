package net.n2oapp.cache.template;

/**
 * Обработка событий при работе с кешем
 */
@FunctionalInterface
public interface CacheCallback<T> {
    /**
     * Получить объект из источника, при отсутствии его в кеше
     * @return объект для кеша
     */
    T doInCacheMiss();

    /**
     * Сделать что-то после получения объекта из кеша
     * @param t объект из кеша
     */
    default void doInCacheHit(T t) {};
}
