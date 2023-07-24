package net.n2oapp.framework.autotest.api.component.cell;

import java.time.Duration;

/**
 * Ячейка таблицы с рейтингом для автотестирования
 */
public interface RatingCell extends Cell {

    /**
     * Проверка максимально возможного значения
     * @param max максимальное ожидаемое значение
     */
    void shouldHaveMax(int max);

    /**
     * Проверка актуального значения
     * @param value ожидаемое актуальное значение
     */
    void shouldHaveValue(String value, Duration... duration);

    /**
     * Выбор значения рейтинга
     * @param value значение рейтинга
     */
    void value(String value);
}
