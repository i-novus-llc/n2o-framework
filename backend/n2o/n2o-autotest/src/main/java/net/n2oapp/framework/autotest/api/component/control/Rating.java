package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода и отображения рейтинга для автотестирования
 */
public interface Rating extends Control {

    void val(String value);

    void shouldHaveValue(String value);
}
