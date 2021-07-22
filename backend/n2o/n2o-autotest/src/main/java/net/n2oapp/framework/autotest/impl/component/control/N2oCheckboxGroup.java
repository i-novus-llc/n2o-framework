package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;

/**
 * Компонент группы чекбоксов для автотестирования
 */
public class N2oCheckboxGroup extends N2oControl implements CheckboxGroup {

    @Override
    public void shouldHaveValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldBeEmpty() {
        element().$$(".custom-control .n2o-input").find(Condition.checked).shouldNotBe(Condition.exist);
    }

    @Override
    public void check(String label) {
        if (!inputElement(label).isSelected())
            inputElement(label).shouldBe(Condition.exist).parent().click();
    }

    @Override
    public void uncheck(String label) {
        if (inputElement(label).isSelected())
            inputElement(label).shouldBe(Condition.exist).parent().click();
    }

    @Override
    public void shouldBeChecked(String label) {
        inputElement(label).shouldBe(Condition.checked);
    }

    @Override
    public void shouldBeUnchecked(String label) {
        inputElement(label).shouldNotBe(Condition.checked);
    }

    @Override
    public void shouldHaveOptions(String... options) {
        element().$$(".custom-control-label").shouldHave(CollectionCondition.exactTexts(options));
    }

    private SelenideElement inputElement(String label) {
        return element().$$(".custom-control").findBy(Condition.text(label)).$(".n2o-input");
    }
}
