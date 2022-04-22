package net.n2oapp.framework.autotest.api.component.snippet;

/**
 * Компонент ввода html поля для автотестирования
 */
public interface Html extends Snippet {

    void shouldHaveElement(String cssSelector);

}
