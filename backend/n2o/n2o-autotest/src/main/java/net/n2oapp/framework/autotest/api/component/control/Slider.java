package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ползунок для автотестирования
 */
public interface Slider extends Control {

    /**
     * Установка значения с шагом 1
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Установка значения с шагом
     * @param value устанавливаемое значение
     * @param step шаг
     */
    void setValue(String value, int step);

    /**
     * Установка значения у левого ползунка
     * @param value значение
     */
    void setLeftValue(String value);

    /**
     * Установка значения с шагом у левого ползунка
     * @param value значение
     * @param step шаг
     */
    void setLeftValue(String value, int step);

    /**
     * Установка значения у правого ползунка
     * @param value значение
     */
    void setRightValue(String value);

    /**
     * Установка значения с шагом у правого ползунка
     * @param value значение
     * @param step шаг
     */
    void setRightValue(String value, int step);

    /**
     * Проверка значения у левого ползунка
     * @param value ожидаемое значение
     */
    void shouldHaveLeftValue(String value);

    /**
     * Проверка значения у правого ползунка
     * @param value ожидаемое значение
     */
    void shouldHaveRightValue(String value);
}
