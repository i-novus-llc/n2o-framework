package net.n2oapp.framework.autotest.api.component.widget.html;

import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

import java.util.Map;

/**
 * Виджет Html для автотестирования
 */
public interface HtmlWidget extends StandardWidget {

    void shouldHaveElement(String cssSelector);
    void shouldHaveElementWithAttributes(String cssSelector, Map<String, String> attributes);

}
