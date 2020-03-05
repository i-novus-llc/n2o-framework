package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с иконкой для автотестирования
 */
public interface IconCell extends Cell {
    void iconShouldBe(String icon);
    void textShouldHave(String text);
}
