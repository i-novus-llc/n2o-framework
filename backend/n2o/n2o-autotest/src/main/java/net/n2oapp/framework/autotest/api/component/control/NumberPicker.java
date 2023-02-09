package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода числа из диапазона
 */
public interface NumberPicker extends Control {
    void setValue(String value);

    void clear();

    void click();

    void clickMinusStepButton();

    void shouldHaveEnableMinusStepButton();

    void shouldHaveDisableMinusStepButton();

    void clickPlusStepButton();

    void plusStepButtonShouldBeEnabled();

    void plusStepButtonShouldBeDisabled();

    void shouldHaveMin(String min);

    void shouldHaveMax(String max);

    void shouldHaveStep(String step);
}
