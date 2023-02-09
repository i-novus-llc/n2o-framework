package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Кнопка с ссылкой для автотестирования
 */
public interface AnchorMenuItem extends MenuItem, Badge {

    void shouldHaveIcon();

    void shouldHaveIconWithCssClass(String cssClass);

    void shouldHaveBadge();

    void shouldHaveBadgeWithText(String text);

    void shouldHaveBadgeWithColor(String color);

    void shouldHaveUrl(String url);
}
