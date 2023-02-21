package net.n2oapp.framework.autotest.api.component.control;

/**
 * Поле ввода многострочного текста для автотестирования
 */
public interface TextArea extends Control {

    /**
     * @return Значение поля
     */
    String getValue();

    /**
     * Установка значения в поле
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Проверка соответствия текста подсказки у поля ввода
     * @param value ожидаемый текст
     */
    void shouldHavePlaceholder(String value);
}
