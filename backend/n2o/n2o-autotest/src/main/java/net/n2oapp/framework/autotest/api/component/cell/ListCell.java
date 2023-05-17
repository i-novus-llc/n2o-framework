package net.n2oapp.framework.autotest.api.component.cell;

/**
 * Ячейка таблицы со списком для автотестирования
 */
public interface ListCell extends Cell {
    /**
     * Проверяет количество баджей(Badge) в ячейке
     * @param count ожидаемое количество баджей
     */
    void shouldHaveSize(int count);

    /**
     * Проверяет текст в бадже(Badge)
     * @param index номер проверяемого баджа в ячейке
     * @param val ожидаемый текст баджа
     */
    void shouldHaveText(int index, String val);
}


