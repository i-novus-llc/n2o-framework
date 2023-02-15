package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Кнопка с ссылкой для автотестирования
 */
public interface AnchorMenuItem extends MenuItem, Badge {

    /**
     * Проверка наличия иконки
     */
    void shouldHaveIcon();

    /**
     * Проверка иконки на соответствие
     * @param cssClass ожидаемый класс иконки
     */
    void shouldHaveIconWithCssClass(String cssClass);

    /**
     * Проверка наличия баджа внутри кнопки
     */
    void shouldHaveBadge();

    /**
     * Проверка текста баджа на соответствие
     * @param text ожидаемый текст баджа
     */
    void shouldHaveBadgeWithText(String text);

    /**
     * Проверка цвета баджа на соответствие
     * @param color ожидаемый текст баджа
     */
    void shouldHaveBadgeWithColor(String color);

    /**
     * Проверка ссылки на соответствие
     * @param url ожидаемая ссылка
     */
    void shouldHaveUrl(String url);
}
