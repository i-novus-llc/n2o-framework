package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент редактирования кода для автотестирования
 */
public interface CodeEditor extends Control {

    void val(String value);

    void shouldHaveValue(String value, int line);
}
