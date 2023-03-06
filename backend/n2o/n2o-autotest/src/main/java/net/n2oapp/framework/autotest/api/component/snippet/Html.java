package net.n2oapp.framework.autotest.api.component.snippet;

import java.util.Map;

/**
 * Компонент ввода html поля для автотестирования
 */
public interface Html extends Snippet {

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
