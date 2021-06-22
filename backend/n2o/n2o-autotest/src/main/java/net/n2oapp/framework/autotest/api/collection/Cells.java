package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;

/**
 * Ячейки таблицы для автотестирования
 */
public interface Cells extends ComponentsCollection {
    TextCell cell(int index);

    <T extends Cell> T cell(int index, Class<T> componentClass);

    <T extends Cell> T cell(Condition findBy, Class<T> componentClass);

    void click();

    void hover();

    void shouldHaveColor(Colors color);
}
