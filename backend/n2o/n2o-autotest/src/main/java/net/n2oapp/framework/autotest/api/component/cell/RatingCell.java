package net.n2oapp.framework.autotest.api.component.cell;

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
    void shouldHaveValue(String value);

    /**
     * Выбор значения рейтинга
     * @param regex регулярное выражение соответствующее значению рейтинга
     */
    void clickOnValue(String regex);
}
