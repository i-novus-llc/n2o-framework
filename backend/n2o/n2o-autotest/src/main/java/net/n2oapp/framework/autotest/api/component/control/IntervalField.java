package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.field.Field;

/**
 * Компонент ввода интервала для автотестирования
 */
public interface IntervalField extends Field {
    /**
     * Возвращает поле ввода начала
     * @param control тип возвращаемого поля
     * @return компонент поля ввода для автотестирования
     */
    <T extends Control> T begin(Class<T> control);

    /**
     * Возвращает поле ввода конца
     * @param control тип возвращаемого поля
     * @return компонент поля ввода для автотестирования
     */
    <T extends Control> T end(Class<T> control);
}
