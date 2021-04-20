package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста с маской для автотестирования
 */
public interface MaskedInput extends Control {
    String val();

    void val(String value);

    void shouldHavePlaceholder(String value);

    void shouldHaveMeasure();

    void measureShouldHaveText(String text);
}
