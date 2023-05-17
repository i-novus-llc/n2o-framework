package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.PasswordControl;
import org.openqa.selenium.Keys;

/**
 * Поле ввода пароля для автотестирования
 */
public class N2oPasswordControl extends N2oControl implements PasswordControl {

    @Override
    public String getValue() {
        SelenideElement input = inputElement();

        return input.exists() ? input.getValue() : cellInputElement().text();
    }

    @Override
    public void setValue(String value) {
        inputElement().setValue(value).pressEnter();
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
    public void shouldHaveValue(String value) {
        SelenideElement input = inputElement();
        boolean logicResult = value == null || value.isEmpty();

        if (input.exists())
            input.shouldHave(logicResult ? Condition.empty : Condition.value(value));
        else
            cellInputElement().shouldHave(logicResult ? Condition.empty : Condition.text(value));
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        Condition condition = Condition.attribute("placeholder", value);
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(condition);
        else
            cellInputElement().shouldHave(condition);
    }

    @Override
    public void clickEyeButton() {
        element().parent().$(".n2o-input-password-toggler")
                .hover()
                .shouldBe(Condition.visible)
                .click();
    }

    @Override
    public void shouldHaveVisiblePassword() {
        SelenideElement input = inputElement();
        Condition condition = Condition.attribute("type", "text");
        if (input.exists())
            input.shouldHave(condition);
        else
            cellInputElement().shouldHave(condition);
    }

    @Override
    public void shouldNotHaveVisiblePassword() {
        SelenideElement input = inputElement();
        Condition condition = Condition.attribute("type", "password");

        if (input.exists())
            input.shouldHave(condition);
        else
            cellInputElement().shouldHave(condition);
    }

    protected SelenideElement inputElement() {
        return element().shouldBe(Condition.exist).parent().$(".n2o-input");
    }

    protected SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }
}
