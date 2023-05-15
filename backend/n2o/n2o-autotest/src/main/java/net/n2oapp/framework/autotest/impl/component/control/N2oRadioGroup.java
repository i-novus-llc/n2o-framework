package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;

/**
 * Компонент радиокнопок для автотестирования
 */
public class N2oRadioGroup extends N2oControl implements RadioGroup {

    @Override
    public void shouldHaveValue(String value) {
        shouldBeChecked(value);
    }

    @Override
    public void shouldBeEmpty() {
        element().$(".checked").shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeChecked(String label) {
        element().$(".checked span").shouldHave(Condition.text(label));
    }

    @Override
    public void check(String label) {
        element().$$("span").findBy(Condition.text(label)).click();
    }

    @Override
    public void shouldHaveOptions(String... labels) {
        radioInput().shouldHave(CollectionCondition.exactTexts(labels));
    }

    @Override
    public void shouldHaveType(RadioType type) {
        element().shouldHave(Condition.cssClass(String.format("n2o-radio-group-%s", type.name().toLowerCase())));
    }

    protected ElementsCollection radioInput() {
        return element().$$(".n2o-radio-input");
    }
}
