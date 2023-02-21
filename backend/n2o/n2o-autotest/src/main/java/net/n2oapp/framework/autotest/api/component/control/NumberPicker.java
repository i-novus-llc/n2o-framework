package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода числа из диапазона
 */
public interface NumberPicker extends Control {
    /**
     * Установка значения в поле ввода
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Очистка поля
     */
    void clear();

    /**
     * Клик по полю
     */
    void click();

    /**
     * Уменьшение значения
     */
    void clickMinusStepButton();

    /**
     * Увеличение значения
     */
    void clickPlusStepButton();

    /**
     * Проверка доступности кнопки для уменьшения значения
     */
    void minusStepButtonShouldBeEnabled();

    /**
     * Проверка доступности кнопки для увеличения значения
     */
    void plusStepButtonShouldBeEnabled();

    /**
     * Проверка недоступности кнопки для уменьшения значения
     */
    void minusStepButtonShouldBeDisabled();

    /**
     * Проверка недоступности кнопки для увеличения значения
     */
    void plusStepButtonShouldBeDisabled();

    /**
     * Проверка соответствия минимального значения
     * @param min ожидаемое минимальное значение
     */
    void shouldHaveMin(String min);

    /**
     * Проверка соответствия максимального значения
     * @param max ожидаемое максимального значение
     */
    void shouldHaveMax(String max);

    /**
     * Проверка соответствия шага изменения значения
     * @param step ожидаемый шаг
     */
    void shouldHaveStep(String step);
}
