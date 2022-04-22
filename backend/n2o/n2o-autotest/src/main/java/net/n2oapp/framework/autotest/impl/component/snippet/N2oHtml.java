package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.snippet.Html;

public class N2oHtml extends N2oSnippet implements Html {

    @Override
    public void shouldHaveElement(String cssSelector) {
        element().$("div").$(cssSelector).shouldBe(Condition.exist);
    }

    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.text(text));
    }
}
