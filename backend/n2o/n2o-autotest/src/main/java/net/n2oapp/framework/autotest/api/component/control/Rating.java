package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода и отображения рейтинга для автотестирования
 */
public interface Rating extends Control {

    /**
     * Установка рейтинга в поле
     * @param value устанавливаемый рейтинг
     */
    void setValue(String value);
}
