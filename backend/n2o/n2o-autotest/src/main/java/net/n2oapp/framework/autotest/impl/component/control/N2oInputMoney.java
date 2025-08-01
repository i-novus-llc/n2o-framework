package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.InputMoneyControl;
import org.openqa.selenium.Keys;

import java.time.Duration;

/**
 * Компонент ввода денежных единиц для автотестирования
 */
public class N2oInputMoney extends N2oControl implements InputMoneyControl {

    protected static final String INPUT = ".n2o-input-money";

    @Override
    public String getValue() {
        SelenideElement input = inputElement();
        if (input.exists())
            return input.getValue();
        return cellInputElement().text();
    }

    @Override
    public void setValue(String value) {
        element().parent().$(INPUT).setValue(value).pressEnter();
    }

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(Condition.empty);
        else
            cellInputElement().shouldHave(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        boolean b = value == null || value.isEmpty();
        SelenideElement input = inputElement();

        if (input.exists())
            should(b ? Condition.empty : Condition.exactValue(value), input, duration);
        else
            should(b ? Condition.empty : Condition.exactText(value), cellInputElement(), duration);
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        WebElementCondition condition = Condition.attribute("placeholder", value);
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(condition);
        else
            cellInputElement().shouldHave(condition);
    }

    @Override
    public void click() {
        inputElement().click();
    }

    @Override
    public void backspace() {
        inputElement().sendKeys(Keys.BACK_SPACE);
    }

    protected SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    protected SelenideElement inputElement() {
        return element().shouldBe(Condition.exist)
                .parent().$(INPUT);
    }
}
