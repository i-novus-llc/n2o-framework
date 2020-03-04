package net.n2oapp.framework.autotest.api.component.control;

import java.util.Map;

/**
 * Компонент ввода html для автотестирования
 */
public interface Html extends Control {
    void shouldHaveElement(String cssSelector);

    void shouldHaveElementWithAttributes(String cssSelector, Map<String, String> attributes);
}
