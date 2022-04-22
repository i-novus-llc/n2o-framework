package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.snippet.Html;

import java.util.Map;

public class N2oHtml extends N2oSnippet implements Html {

    @Override
    public void shouldHaveElement(String cssSelector) {
        element().$("div").$(cssSelector).shouldBe(Condition.exist);
    }

    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveElementWithAttributes(String cssSelector, Map<String, String> attributes) {
        SelenideElement element = element().$("div").$(cssSelector).shouldBe(Condition.exist);
        attributes.entrySet().stream().forEach(e -> element.shouldHave(Condition.attribute(e.getKey(), e.getValue())));
    }
}
