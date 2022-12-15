package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.Pills;

/**
 * Компонент Таблетки для автотестирования
 */
public class N2oPills extends N2oControl implements Pills {

    @Override
    public void shouldBeEmpty() {
        element().$$(".nav-link.active").shouldHaveSize(0);
    }

    @Override
    public void shouldHaveValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void check(String label) {
        if (!itemLink(label).is(Condition.cssClass("active")))
            item(label).shouldBe(Condition.exist).click();
    }

    @Override
    public void uncheck(String label) {
        if (itemLink(label).is(Condition.cssClass("active")))
            item(label).shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldBeChecked(String label) {
        itemLink(label).shouldHave(Condition.cssClass("active"));
    }

    @Override
    public void shouldBeUnchecked(String label) {
        itemLink(label).shouldNotHave(Condition.cssClass("active"));
    }

    @Override
    public void shouldHaveOptions(String... options) {
        element().$$(".nav-item").shouldHave(CollectionCondition.exactTexts(options));
    }

    private SelenideElement item(String label) {
        return element().$$(".nav-item").findBy(Condition.text(label));
    }

    private SelenideElement itemLink(String label) {
        return item(label).$(".nav-link");
    }
}
