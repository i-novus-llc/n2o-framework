package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода денежных единиц для автотестирования
 */
public interface InputMoneyControl extends Control {
    String val();

    void val(String value);

    void shouldHavePlaceholder(String value);
}
