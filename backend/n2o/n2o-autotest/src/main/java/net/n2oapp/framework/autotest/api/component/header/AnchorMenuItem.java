package net.n2oapp.framework.autotest.api.component.header;

/**
 * Кнопка с ссылкой для автотестирования
 */
public interface AnchorMenuItem extends MenuItem {

    void shouldHaveIcon();

    void iconShouldHaveClass(String clazz);

    void shouldHaveBadge();

    void badgeShouldHaveValue(String value);

    void urlShouldHave(String url);
}
