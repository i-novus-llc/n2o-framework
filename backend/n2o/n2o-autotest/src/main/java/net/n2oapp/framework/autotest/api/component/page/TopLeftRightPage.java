package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;

/**
 * Страница с тремя регионами для автотестирования
 */
public interface TopLeftRightPage extends Page {
    /**
     * Возвращает регионы из верхней части страницы
     * @return Регионы для автотестирования
     */
    Regions top();

    /**
     * Возвращает регионы из левой части страницы
     * @return Регионы для автотестирования
     */
    Regions left();

    /**
     * Возвращает регионы из правой части страницы
     * @return Регионы для автотестирования
     */
    Regions right();

    /**
     * Проверка наличия кнопки скрола в верх страницы
     */
    void shouldHaveScrollToTopButton();

    /**
     * Проверка наличия кнопки скрола в низ страницы
     */
    void shouldNotHaveScrollToTopButton();

    /**
     * Клик по кнопке скрола в верх страницы
     */
    void clickScrollToTopButton();
}
