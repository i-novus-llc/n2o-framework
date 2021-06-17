package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
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
    public void shouldHaveOptions(String... options) {
        element().$$(".n2o-radio-input").shouldHave(CollectionCondition.exactTexts(options));
    }
}
