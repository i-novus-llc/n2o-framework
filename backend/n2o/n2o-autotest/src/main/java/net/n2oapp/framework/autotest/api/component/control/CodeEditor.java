package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент редактирования кода для автотестирования
 */
public interface CodeEditor extends Control {

    /**
     * Установка значения в поле
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Проверка соответствия значения в строке
     * @param value ожидаемое значение
     * @param line проверяемая строка
     */
    void shouldHaveValue(String value, int line);
}
