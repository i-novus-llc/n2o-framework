package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.Tooltip;

/**
 * Ячейка таблицы с иконкой для автотестирования
 */
public interface IconCell extends Cell {
    void iconShouldBe(String icon);

    void textShouldHave(String text);

    void hover();

    Tooltip tooltip();
}
