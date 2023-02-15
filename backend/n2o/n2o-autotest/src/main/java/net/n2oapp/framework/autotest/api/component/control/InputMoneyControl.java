package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода денежных единиц для автотестирования
 */
public interface InputMoneyControl extends Control {

    /**
     * @return значение из поля ввода
     */
    String getValue();

    /**
     * Установка значения в поле ввода
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Проверка соответствия текста подсказки для ввода
     * @param value ожидаемый текст
     */
    void shouldHavePlaceholder(String value);
}
