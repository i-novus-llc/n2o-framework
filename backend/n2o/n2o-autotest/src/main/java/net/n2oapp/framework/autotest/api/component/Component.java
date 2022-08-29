package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.Condition;

/**
 * Любой визуальный компонент для автотестирования
 */
public interface Component extends Element {

    void shouldExists();
    void shouldNotExists();

    void shouldBeVisible();
    void shouldBeHidden();

    default void shouldHaveCssClass(String cssClass) {
        element().shouldHave(Condition.cssClass(cssClass));
    }
}
