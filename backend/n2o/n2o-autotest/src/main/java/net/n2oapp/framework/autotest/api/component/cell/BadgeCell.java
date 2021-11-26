package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.Colors;

/**
 * Ячейка таблицы с текстом для автотестирования
 */
public interface BadgeCell extends Cell {
    void colorShouldBe(Colors color);

    void textShouldHave(String text);

    void shouldHaveStyle(String style);

    void shouldHaveCssClass(String cssClass);
}
