package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.control.Progress;

/**
 * Компонент отображения прогресса для автотестирования
 */
public class N2oProgress extends N2oControl implements Progress {

    @Override
    public void shouldBeEmpty() {
        progressBar().shouldNot(Condition.exist);
    }

    @Override
    public void shouldHaveValue(String value) {
        progressBar().shouldHave(Condition.attribute("aria-valuenow", value));
    }

    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.attribute("barText", text));
    }

    @Override
    public void shouldHaveMax(String max) {
        progressBar().shouldHave(Condition.attribute("aria-valuemax", max));
    }

    @Override
    public void shouldBeAnimated() {
        progressBar().shouldHave(Condition.cssClass("progress-bar-animated"));
    }

    @Override
    public void shouldBeStriped() {
        progressBar().shouldHave(Condition.cssClass("progress-bar-striped"));
    }

    @Override
    public void shouldHaveColor(Colors color) {
        progressBar().shouldHave(Condition.cssClass(color.name("bg-")));
    }

    protected SelenideElement progressBar() {
        return element().$(".progress-bar");
    }
}
