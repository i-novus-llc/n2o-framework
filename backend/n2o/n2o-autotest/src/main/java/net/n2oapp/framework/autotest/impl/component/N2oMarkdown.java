package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Markdown;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oSnippet;

import java.time.Duration;

public class N2oMarkdown extends N2oSnippet implements Markdown {

    @Override
    public StandardButton button(String label) {
        return N2oSelenide.component(element().$$(".n2o-markdown-button").findBy(Condition.text(label)), StandardButton.class);
    }

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.exactText(text), duration);
    }

    @Override
    public void shouldHaveElement(String cssSelector) {
        element().$(cssSelector).shouldBe(Condition.exist);
    }
}
