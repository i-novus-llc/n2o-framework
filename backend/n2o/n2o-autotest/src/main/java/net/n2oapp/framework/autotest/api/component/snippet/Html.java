package net.n2oapp.framework.autotest.api.component.snippet;

import java.util.Map;

/**
 * Компонент ввода html поля для автотестирования
 */
public interface Html extends Snippet {

    void shouldHaveElement(String cssSelector);
    void shouldHaveElementWithAttributes(String cssSelector, Map<String, String> attributes);

}
