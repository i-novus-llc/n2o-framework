package net.n2oapp.framework.autotest.component;

import com.codeborne.selenide.SelenideElement;

public interface Component {
    SelenideElement element();
    void setElement(SelenideElement parentElement);

    void shouldExists();
    void shouldNotExists();
}
