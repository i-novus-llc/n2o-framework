package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.snippet.Status;

/**
 * Компонент отображения статуса для автотестирования
 */
public class N2oStatus extends N2oSnippet implements Status {
    @Override
    public void shouldHaveText(String text) {
        element().$(".n2o-status-text_text").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveLeftPositionText() {
        element().shouldHave(Condition.cssClass("n2o-status-text__left"));
    }

    @Override
    public void shouldHaveRightPositionText() {
        element().shouldHave(Condition.cssClass("n2o-status-text__right"));
    }

    @Override
    public void shouldHaveColor(Colors color) {
        element().$(".n2o-status-text_icon__right, .n2o-status-text_icon__left")
                .shouldHave(Condition.cssClass(color.name("bg-")));
    }
}
