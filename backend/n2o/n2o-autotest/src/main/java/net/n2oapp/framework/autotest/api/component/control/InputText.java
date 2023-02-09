package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста для автотестирования
 */
public interface InputText extends Control {
    String getValue();

    void setValue(String value);

    void clear();

    void shouldHavePlaceholder(String placeholder);

    void click();

    void clickPlusStepButton();

    void clickMinusStepButton();

    void shouldHaveMeasure();

    void shouldHaveMeasureText(String text);
}
