package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ползунок для автотестирования
 */
public interface Slider extends Control {
    void setValue(String value);

    void setValue(String value, int step);

    void setValueInLeft(String value);

    void setValueInLeft(String value, int step);

    void setValueInRight(String value);

    void setValueInRight(String value, int step);

    void shouldHaveLeftValue(String value);

    void shouldHaveRightValue(String value);
}
