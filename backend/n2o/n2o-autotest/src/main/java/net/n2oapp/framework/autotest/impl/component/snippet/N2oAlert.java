package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;

/**
 * Компонент предупреждения для автотестирования
 */
public class N2oAlert extends N2oSnippet implements Alert {

    public N2oAlert() {
    }

    public N2oAlert(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void shouldHaveColor(Colors color) {
        element().shouldHave(Condition.cssClass(color.name("alert-")));
    }

    @Override
    public void shouldHaveTitle(String text) {
        element().$(".n2o-alert-segment__title").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveUrl(String url) {
        element().$("a").shouldHave(Condition.attribute("href", url));
    }

    @Override
    public void shouldHaveCloseButton() {
        element().$(".n2o-alert-segment__icon-close").shouldHave(Condition.exist);
    }

    @Override
    public void shouldHavePlacement(Placement placement) {
        element().parent().should(Condition.cssClass(placement.name()));
    }

    @Override
    public void shouldHaveStacktrace() {
        element().should(Condition.cssClass("with-details"));
    }

    @Override
    public void shouldHaveText(String text) {
        element().$(".n2o-alert-segment__text").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveTimestamp(String timestamp) {
        element().$(".n2o-alert-segment__timestamp").shouldHave(Condition.text(timestamp));
    }

    @Override
    public void shouldHaveTimeout(Integer timeout) throws InterruptedException {
        this.shouldExists();
        Thread.sleep(timeout);
        this.shouldNotExists();
    }
}
