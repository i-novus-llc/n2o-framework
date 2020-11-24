package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода числа из диапазона
 */
public interface NumberPicker extends Control {
    void val(String value);

    void clear();

    void clickMinusStepButton();

    void minusStepButtonShouldBeEnabled();

    void minusStepButtonShouldBeDisabled();

    void clickPlusStepButton();

    void plusStepButtonShouldBeEnabled();

    void plusStepButtonShouldBeDisabled();

    void minShouldBe(String val);

    void maxShouldBe(String val);

    void stepShouldBe(String val);
}
