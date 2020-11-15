package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода числа из диапазона
 */
public interface NumberPicker extends Control {
    String val();

    void val(String value);

    void clear();

    void clickMinusStepButton();

    void clickPlusStepButton();

    void minShouldBe(String val);

    void maxShouldBe(String val);

    void stepShouldBe(String val);

}
