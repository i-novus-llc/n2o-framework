package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.InputMoneyControl;
import org.openqa.selenium.Keys;

/**
 * Компонент ввода денежных единиц для автотестирования
 */
public class N2oInputMoney extends N2oControl implements InputMoneyControl {

    @Override
    public String getValue() {
        SelenideElement input = inputElement();
        if (input.exists())
            return input.getValue();
        return editableCell().text();
    }

    @Override
    public void setValue(String value) {
        element().parent().$(".n2o-input-money").setValue(value).pressEnter();
    }

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(Condition.empty);
        else
            editableCell().shouldHave(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value) {
        boolean b = value == null || value.isEmpty();
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(b ? Condition.empty : Condition.value(value));
        else
            editableCell().shouldHave(b ? Condition.empty : Condition.text(value));
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        Condition condition = Condition.attribute("placeholder", value);
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(condition);
        else
            editableCell().shouldHave(condition);
    }

    protected SelenideElement editableCell() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    protected SelenideElement inputElement() {
        return element().shouldBe(Condition.exist)
                .parent().$(".n2o-input-money");
    }
}
