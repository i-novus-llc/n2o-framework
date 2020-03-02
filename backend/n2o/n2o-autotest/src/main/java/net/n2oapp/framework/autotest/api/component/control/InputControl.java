package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста (input-text) для автотестирования
 */
public interface InputControl extends Control {
    String val();

    void val(String value);

    void shouldHaveValue(String value);

    void shouldHavePlaceholder(String placeholder);

    void shouldHaveLength(String length);

    void shouldHaveMin(String min);

    void shouldHaveMax(String max);

    void shouldHaveStep(String step);

    void clickPlusStepButton();

    void clickMinusStepButton();
}
