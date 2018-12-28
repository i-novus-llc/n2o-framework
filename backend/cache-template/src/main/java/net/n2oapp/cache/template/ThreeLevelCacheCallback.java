package net.n2oapp.cache.template;


/**
 * Обработка событий в трехуровневом кеше
 */
public interface ThreeLevelCacheCallback<F, S, T> {

    /**
     * Получить объект из источника, при отсутствии его в кеше первого уровня
     * @param valueFromSecondLevelCache объект из кеша второго уровня
     * @return объект для кеша первого уровня
     */
    F doInFirstLevelCacheMiss(S valueFromSecondLevelCache);

    /**
     * Получить объект, при отсутствии его в кеше второго уровня
     * @param valueFromThirdLevelCache объект из кеша третьего уровня
     * @return объект для кеша второго уровня
     */
    S doInSecondLevelCacheMiss(T valueFromThirdLevelCache);

    /**
     * Получить объект, при отсутствии его в кеше третьего уровня
     * @return объект для кеша третьего уровня
     */
    T doInThirdLevelCacheMiss();

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

    /**
     * Сделать что-то после получения объекта из кеша третьего уровня
     * @param valueFromThirdLevelCache объект из кеша третьего уровня
     */
    default void doInThirdLevelCacheHit(T valueFromThirdLevelCache) {};
}
