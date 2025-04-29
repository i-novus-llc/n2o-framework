package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

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
    public void shouldHaveColor(ColorsEnum color) {
        element().shouldHave(Condition.cssClass(String.format("alert-%s", color.name().toLowerCase())));
    }

    @Override
    public void shouldHaveTitle(String text, Duration... duration) {
        should(Condition.text(text), element().$(".n2o-alert-segment__title"), duration);
    }

    @Override
    public void shouldHaveUrl(String url) {
        element().$("a").shouldHave(Condition.attribute("href", url));
    }

    @Override
    public void shouldHaveStacktrace() {
        element().should(Condition.cssClass("with-details"));
    }

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.text(text), element().$(".n2o-alert-segment__text"), duration);
    }

    @Override
    public void shouldHaveTimestamp(String timestamp, Duration... duration) {
        should(Condition.text(timestamp), element().$(".n2o-alert-segment__timestamp"), duration);
    }

    @Override
    public void shouldNotExists(Duration... duration) {
        should(Condition.not(Condition.exist), element(), duration);
    }

    @Override
    public CloseButton closeButton() {
        return new N2oCloseButton(element().$(".n2o-alert-segment__icon-close"));
    }

    @Override
    public void click() {
        element().click();
    }

    public static class N2oCloseButton extends N2oComponent implements CloseButton {

        public N2oCloseButton(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void click() {
            element().click();
        }
    }
}
