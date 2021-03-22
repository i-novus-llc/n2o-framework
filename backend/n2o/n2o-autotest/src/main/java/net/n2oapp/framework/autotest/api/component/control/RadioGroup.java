package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент радиокнопок для автотестирования
 */
public interface RadioGroup extends Control {

    void shouldBeChecked(String label);

    void check(String label);

    void shouldHaveOptions(String... options);
}
