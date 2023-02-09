package net.n2oapp.framework.autotest.api.component.control;

/**
 * Поле ввода многострочного текста для автотестирования
 */
public interface TextArea extends Control {
    String getValue();

    void setValue(String value);

    void shouldHavePlaceholder(String value);
}
