package net.n2oapp.framework.autotest.api.component.widget.html;

import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

/**
 * Виджет Html для автотестирования
 */
public interface HtmlWidget extends StandardWidget {

    void shouldHaveElement(String cssSelector);
}
