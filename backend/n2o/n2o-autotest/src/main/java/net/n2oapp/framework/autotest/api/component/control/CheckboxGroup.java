package net.n2oapp.framework.autotest.api.component.control;


/**
 * Группа чекбоксов для автотестирования
 */
public interface CheckboxGroup extends Control {

    void check(String label);

    void uncheck(String label);

    void shouldBeChecked(String label);

    void shouldBeUnchecked(String label);

}
