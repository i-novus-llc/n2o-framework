package net.n2oapp.engine.factory.locator;

import net.n2oapp.engine.factory.MultiEngineFactory;

import java.util.function.BiPredicate;

/**
 * Поиск движков по типу
 * @param <G> Тип движка
 */
public interface EngineLocator<G> {

    /**
     * Отобрать список движков по условию
     * @param predicate Условие
     * @return Список движков, удовлетворяющих условию
     */
    <T> MultiEngineFactory<T, G> locate(BiPredicate<G, T> predicate);

}
