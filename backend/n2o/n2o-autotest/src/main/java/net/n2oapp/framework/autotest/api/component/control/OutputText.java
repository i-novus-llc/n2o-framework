package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент вывода однострочного текста для автотестирования
 */
public interface OutputText extends Control {

    /**
     * Проверка соответствия иконки у поля
     * @param icon ожидаемая иконка
     */
    void shouldHaveIcon(String icon);

    /**
     * Проверка того, что поле не пустое
     */
    void shouldNotBeEmpty();

    /**
     * Проверка того, что поле не содержит ожидаемое значение
     * @param value ожидаемое значение
     */
    void shouldNotHaveValue(String value);

    /**
     * @return Значение из поля
     */
    String getValue();
}
