package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы с Toolbar для автотестирования
 */
public interface ToolbarCell extends Cell {

    void clickMenu(String menuName);

    void itemsShouldBe(int count);

    void itemsTextShouldBe(int index, String text);
}
