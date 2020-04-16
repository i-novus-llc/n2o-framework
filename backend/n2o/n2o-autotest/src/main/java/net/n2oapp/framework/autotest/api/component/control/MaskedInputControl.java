package net.n2oapp.framework.autotest.api.component.control;

/**
 * Ввод текста с маской для автотестирования
 */
public interface MaskedInputControl extends Control {
    String val();

    void val(String value);

    void shouldHavePlaceholder(String value);

    void shouldHaveMeasure();

    void measureShouldHaveText(String text);
}
