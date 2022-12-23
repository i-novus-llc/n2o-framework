package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент вывода однострочного текста для автотестирования
 */
public interface OutputText extends Control {

    void shouldHaveIcon(String icon);

    void shouldNotBeEmpty();

    void shouldNotHaveValue(String value);

    String getValue();
}
