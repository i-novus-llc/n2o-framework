package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода html для автотестирования
 */
public interface Html extends Control {
    void shouldHaveValue(String value);
}
