package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент отображения прогресса для автотестирования
 */
public interface Progress extends Control {

    /**
     * Проверка соответствия текста
     * @param text ожидаемый текст
     */
    void shouldHaveText(String text);

    /**
     * Проверка соответствия максимального значения
     * @param max ожидаемое максимальное значение
     */
    void shouldHaveMax(String max);

    /**
     * Проверка того, что поле анимированное
     */
    void shouldBeAnimated();

    /**
     * Проверка того, что поле с полосками
     */
    void shouldBeStriped();

    /**
     * Проверка соответствия цвета поля
     * @param color ожидаемый цвет поля
     */
    void shouldHaveColor(Colors color);
}
