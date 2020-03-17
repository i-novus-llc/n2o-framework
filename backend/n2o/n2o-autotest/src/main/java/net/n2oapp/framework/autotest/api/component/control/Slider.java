package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ползунок для автотестирования
 */
public interface Slider extends Control {
    void val(String value);

    void val(String value, int step);

    void valLeft(String value);

    void valLeft(String value, int step);

    void valRight(String value);

    void valRight(String value, int step);

    void shouldHaveLeftValue(String value);

    void shouldHaveRightValue(String value);
}
