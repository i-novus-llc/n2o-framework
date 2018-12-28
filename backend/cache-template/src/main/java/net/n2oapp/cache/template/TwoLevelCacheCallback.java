package net.n2oapp.cache.template;


/**
 * Обработка событий в двухуровневом кеше
 */
public interface TwoLevelCacheCallback<F, S> {
    /**
     * Получить объект из источника, при отсутствии его в кеше первого уровня
     * @param valueFromSecondLevelCache - объект из кеша второго уровня
     * @return объект для кеша первого уровня
     */
    F doInFirstLevelCacheMiss(S valueFromSecondLevelCache);

    /**
     * Получить объект, при отсутствии его в кеше второго уровня
     * @return объект для кеша второго уровня
     */
    S doInSecondLevelCacheMiss();

    /**
     * Сделать что-то после получения объекта из кеша первого уровня
     * @param valueFromFirstLevelCache объект из кеша первого уровня
     */
    default void doInFirstLevelCacheHit(F valueFromFirstLevelCache) {};

    /**
     * Сделать что-то после получения объекта из кеша второго уровня
     * @param valueFromSecondLevelCache объект из кеша второго уровня
     */
    default void doInSecondLevelCacheHit(S valueFromSecondLevelCache) {};
}
