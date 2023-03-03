package net.n2oapp.framework.autotest.api.component.widget.html;

import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

import java.util.Map;

/**
 * Виджет Html для автотестирования
 */
public interface HtmlWidget extends StandardWidget {

    /**
     * Проверка наличия элемента по css селектору
     * @param cssSelector css селектр
     */
    void shouldHaveElement(String cssSelector);

    /**
     * Проверка соответствия атрибутов у элемента
     * @param cssSelector css селектор для поиска элемента
     * @param attributes ожидаемые атрибуты
     */
    void shouldHaveElementWithAttributes(String cssSelector, Map<String, String> attributes);

}
