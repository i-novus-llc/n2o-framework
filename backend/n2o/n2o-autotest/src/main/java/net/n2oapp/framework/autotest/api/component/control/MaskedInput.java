package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста с маской для автотестирования
 */
public interface MaskedInput extends Control {
    /**
     * @return значения из поля ввода
     */
    String getValue();

    /**
     * Установка знаения в поле ввода
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Проверка соответствия текста подсказки для ввода
     * @param value ожидаемый текст
     */
    void shouldHavePlaceholder(String value);

    /**
     * Проверка наличия единиц измерения
     */
    void shouldHaveMeasure();

    /**
     * Проверка соответствия единицы измерения
     * @param text ожидаемая единица измерения
     */
    void shouldHaveMeasureText(String text);
}
