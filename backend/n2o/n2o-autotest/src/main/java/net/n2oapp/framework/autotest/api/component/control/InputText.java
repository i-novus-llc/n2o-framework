package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста для автотестирования
 */
public interface InputText extends Control {
    /**
     * @return Значение из поля ввода
     */
    String getValue();

    /**
     * Ввод значение в поле
     * @param value вводимое значение
     */
    void setValue(String value);

    /**
     * Очистка поля
     */
    void clear();

    /**
     * Проверка соответствия текста подсказки у поля ввода
     * @param placeholder ожидаемый текст
     */
    void shouldHavePlaceholder(String placeholder);

    /**
     * Клик по полю
     */
    void click();

    /**
     * Увеличение значения, если тип значений в поле ввода - целые числа
     */
    void clickPlusStepButton();

    /**
     * Уменьшение значения, если тип значений в поле ввода - целые числа
     */
    void clickMinusStepButton();

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
