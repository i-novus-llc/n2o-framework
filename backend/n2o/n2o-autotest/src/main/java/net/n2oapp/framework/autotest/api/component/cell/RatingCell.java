package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с рейтингом для автотестирования
 */
public interface RatingCell extends Cell {

    void maxShouldBe(int max);

    void valueShouldBe(String value);

    void value(String value);
}
