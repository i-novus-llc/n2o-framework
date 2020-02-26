package net.n2oapp.framework.autotest.api.component.header;

/**
 * Кнопка с ссылкой для автотестирования
 */
public interface Link extends MenuItem {
    void shouldHaveUrl(String url);
}
