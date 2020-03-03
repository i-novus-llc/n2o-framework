package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент вывода текста (output-text) для автотестирования
 */
public interface OutputText extends Control {

    void shouldHaveValue(String value);

    void shouldNotHaveValue();

    void shouldHaveIcon(String icon);
}
