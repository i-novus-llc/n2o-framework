package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;

import java.time.Duration;

/**
 * Компонент радиокнопок для автотестирования
 */
public class N2oRadioGroup extends N2oControl implements RadioGroup {

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        shouldBeChecked(value, duration);
    }

    @Override
    public void shouldBeEmpty() {
        element().$(".checked").shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeChecked(String label, Duration... duration) {
        should(Condition.text(label), element().$(".checked span"), duration);
    }

    @Override
    public void check(String label) {
        element().$$("span").findBy(Condition.text(label)).click();
    }

    @Override
    public void shouldHaveOptions(String[] labels, Duration... duration) {
        should(CollectionCondition.exactTexts(labels), radioInput(), duration);
    }

    @Override
    public void shouldNotHaveOptions(Duration... duration) {
        should(CollectionCondition.size(0), radioInput(), duration);
    }

    @Override
    public void shouldHaveType(RadioType type) {
        element().shouldHave(Condition.cssClass(String.format("n2o-radio-group-%s", type.name().toLowerCase())));
    }

    protected ElementsCollection radioInput() {
        return element().$$(".n2o-radio-input");
    }
}
