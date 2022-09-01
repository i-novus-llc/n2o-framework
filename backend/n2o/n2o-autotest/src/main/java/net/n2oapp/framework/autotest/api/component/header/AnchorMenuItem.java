package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Кнопка с ссылкой для автотестирования
 */
public interface AnchorMenuItem extends MenuItem, Badge {

    void shouldHaveIcon();

    void iconShouldHaveCssClass(String clazz);

    void shouldHaveBadge();

    void badgeShouldHaveValue(String value);

    void badgeColorShouldHaveValue(String value);

    void urlShouldHave(String url);
}
