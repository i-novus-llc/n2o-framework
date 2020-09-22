package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;

/**
 * Стандартная реализация компонента предупреждения для автотестирования
 */
public class N2oAlert extends N2oSnippet implements Alert {
    @Override
    public void shouldHaveColor(Colors color) {
        element().shouldHave(Condition.cssClass(color.name("alert-")));
    }

    @Override
    public void footerShouldHaveText(String text) {
        element().$(".n2o-alert-field-footer").shouldHave(Condition.text(text));
    }

    @Override
    public void headerShouldHaveText(String text) {
        element().$(".n2o-alert-field-header").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveText(String text) {
        element().$(".n2o-alert-field-text").shouldHave(Condition.text(text));
    }

}
