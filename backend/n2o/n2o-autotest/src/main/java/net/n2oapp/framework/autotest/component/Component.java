package net.n2oapp.framework.autotest.component;

import com.codeborne.selenide.SelenideElement;

public interface Component {
    SelenideElement element();

    void shouldExists();
    void shouldNotExists();
}
