package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.Html;

import java.util.Map;

/**
 * Компонент ввода html для автотестирования
 */
public class N2oHtml extends N2oControl implements Html {

    @Override
    public void shouldHaveElement(String cssSelector) {
        element().$("div").$(cssSelector).shouldBe(Condition.exist);
    }

    @Override
    public void shouldHaveElementWithAttributes(String cssSelector, Map<String, String> attributes) {
        SelenideElement element = element().$("div").$(cssSelector).shouldBe(Condition.exist);
        attributes.entrySet().stream().forEach(e -> element.shouldHave(Condition.attribute(e.getKey(), e.getValue())));
    }
}
