package net.n2oapp.framework.autotest.api.component.control;

/**
 * Ввод пароля для автотестирования
 */
public interface PasswordControl extends Control {

    /**
     * @return Значение из поля
     */
    String getValue();

    /**
     * Ввод значение в поле
     * @param value вводимое значение
     */
    void setValue(String value);

    /**
     * Проверка соответствия текста подсказки для ввода
     * @param value ожидаемый текст
     */
    void shouldHavePlaceholder(String value);

    /**
     * Клик по кнопке отображения пароля
     */
    void clickEyeButton();

    /**
     * Проверка того, что пароль виден
     */
    void shouldHaveVisiblePassword();

    /**
     * Проверка того, что пароль не виден
     */
    void shouldNotHaveVisiblePassword();
}
