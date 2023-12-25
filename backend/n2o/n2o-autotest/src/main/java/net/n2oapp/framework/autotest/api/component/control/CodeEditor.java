package net.n2oapp.framework.autotest.api.component.control;

import java.time.Duration;

/**
 * Компонент редактирования кода для автотестирования
 */
public interface CodeEditor extends Control {

    /**
     * Установка значения в поле
     *
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Проверка значения в линии
     *
     * @param value ожидаемое значение
     * @param line номер проверяемой линии
     */
    void shouldHaveValue(String value, int line, Duration... duration);

    /**
     * Проверка количества видимых и не видимых линий
     *
     * @param lines - ожидаемое количество линий
     */
    void shouldHaveLines(int lines);

    /**
     * Проверка количества строк в линии
     *
     * @param rows - ожидаемое количество строк в линии
     * @param line - номер проверяемой строки
     */
    void shouldHaveRowsInLine(int rows, int line);
}
