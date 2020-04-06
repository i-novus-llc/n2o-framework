package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.field.Field;

/**
 * Компонент ввода интервала для автотестирования
 */
public interface IntervalField extends Field {
    <T extends Control> T begin(Class<T> control);

    <T extends Control> T end(Class<T> control);
}
