package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы со списком для автотестирования
 */
public interface ListCell extends Cell {

    void shouldHaveSize(int count);

    void shouldHaveText(int index, String val);
}


