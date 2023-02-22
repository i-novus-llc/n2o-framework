package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.SelenideElement;

/**
 * Любой html элемент на странице
 */
public interface Element {

    /**
     * Возвращает селенидовский элемент
     * @return SelenideElement
     */
    SelenideElement element();

    /**
     * Устанавливает в атрибут element необходимый селенидовский элемент
     * @param parentElement селенидовский элемент
     */
    void setElement(SelenideElement parentElement);
}
