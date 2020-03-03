package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент радиокнопок (radio-group) для автотестирования
 */
public interface RadioGroup extends Control {

    void shouldBeChecked(String label);

    void check(String label);
}
