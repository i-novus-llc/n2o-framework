package net.n2oapp.framework.autotest.api.component.snippet;

/**
 * Компонент Image для автотестирования
 */
public interface Image extends Snippet {

    void shouldHaveTitle(String text);

    void shouldHaveDescription(String text);

    void shouldHaveUrl(String url);

    void shouldBeSize(int size);

    void shouldBeAlign(String align);

}
