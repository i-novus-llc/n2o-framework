package net.n2oapp.framework.autotest.api.component.field;

/**
 * Модель компонента ButtonField для автотестирования
 */
public interface ButtonField extends Field {
    void click();

    void shouldBeEnabled();

    void shouldNotBeEnabled();
}
