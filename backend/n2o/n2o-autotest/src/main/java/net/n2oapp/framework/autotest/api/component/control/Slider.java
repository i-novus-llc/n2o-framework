package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ползунок для автотестирования
 */
public interface Slider extends Control {
    void val(String value);

    void valLeft(String value);

    void valRight(String value);

    void shouldHaveValue(String value);

    void shouldHaveLeftValue(String value);

    void shouldHaveRightValue(String value);
}
