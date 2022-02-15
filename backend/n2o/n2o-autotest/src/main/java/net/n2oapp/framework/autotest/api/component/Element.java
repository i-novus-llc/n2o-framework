package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.SelenideElement;

/**
 * Любой html элемент на странице
 */
public interface Element {

    SelenideElement element();
    void setElement(SelenideElement parentElement);
}
