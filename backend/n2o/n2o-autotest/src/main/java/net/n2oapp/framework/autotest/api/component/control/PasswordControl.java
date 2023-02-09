package net.n2oapp.framework.autotest.api.component.control;

/**
 * Ввод пароля для автотестирования
 */
public interface PasswordControl extends Control {

    String getValue();

    void setValue(String value);

    void shouldHavePlaceholder(String value);

    void clickEyeButton();

    void shouldHaveVisiblePassword();

    void shouldNotHaveVisiblePassword();
}
