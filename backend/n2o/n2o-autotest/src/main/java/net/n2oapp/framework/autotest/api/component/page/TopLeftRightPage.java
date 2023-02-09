package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;

/**
 * Страница с тремя регионами для автотестирования
 */
public interface TopLeftRightPage extends Page {
    Regions top();

    Regions left();

    Regions right();

    void shouldHaveScrollToTopButton();

    void shouldNotHaveScrollToTopButton();

    void clickScrollToTopButton();
}
