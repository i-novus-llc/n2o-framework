package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода денежных единиц для автотестирования
 */
public interface InputMoneyControl extends Control {
    String getValue();

    void setValue(String value);

    void shouldHavePlaceholder(String value);
}
