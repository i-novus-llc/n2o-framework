package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста для автотестирования
 */
public interface InputText extends Control {
    String val();

    void val(String value);

    void clear();

    void shouldHavePlaceholder(String placeholder);

    void clickPlusStepButton();

    void clickMinusStepButton();

    void shouldHaveMeasure();

    void measureShouldHaveText(String text);

    void shouldHaveValidationMessage(String text);

    void shouldHaveNotValidationMessage();

    void validationMessageShouldBe(String cssClassStatus);
}
