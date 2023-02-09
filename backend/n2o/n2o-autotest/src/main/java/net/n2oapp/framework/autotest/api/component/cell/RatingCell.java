package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с рейтингом для автотестирования
 */
public interface RatingCell extends Cell {

    void shouldHaveMax(int max);

    void shouldHaveValue(String value);

    void value(String value);
}
