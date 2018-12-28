package net.n2oapp.engine.factory;

/**
 * Типизированный движок
 */
public interface TypicalEngine<T> {
    /**
     * Получить тип движка
     * @return Тип движка
     */
    T getType();
}
