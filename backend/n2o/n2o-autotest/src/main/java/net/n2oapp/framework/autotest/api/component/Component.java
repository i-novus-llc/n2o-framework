package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.SelenideElement;

/**
 * Любой визуальный компонент для автотестирования
 */
public interface Component {
    SelenideElement element();
    void setElement(SelenideElement parentElement);

    void shouldExists();
    void shouldNotExists();

    void shouldBeVisible();
    void shouldBeHidden();
}
