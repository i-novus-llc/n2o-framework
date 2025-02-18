package net.n2oapp.cache.template;

/**
 * Обработчик событий при работе с кэшем
 */
@FunctionalInterface
public interface CacheCallback<T> {
    /**
     * Получить объект из источника при отсутствии его в кэше
     *
     * @return Объект для кэша
     */
    T doInCacheMiss();
}
