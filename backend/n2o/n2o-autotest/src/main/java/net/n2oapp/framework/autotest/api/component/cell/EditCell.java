package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Изменяемая ячейка для автотестирования
 */
public interface EditCell extends Cell {

    /**
     * Возвращает поле ввода для изменения значения в ячейке
     * @param componentClass тип поля ввода
     * @return поле ввода для автотестирования
     */
    <T extends Control> T control(Class<T> componentClass);

    /**
     * Клик по ячейке
     */
    void click();

}
