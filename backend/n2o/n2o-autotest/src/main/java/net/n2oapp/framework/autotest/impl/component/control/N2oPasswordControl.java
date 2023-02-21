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
        SelenideElement elm = inputElement();
        return elm.exists() ? elm.getValue() : element().$(".n2o-editable-cell .n2o-editable-cell-text").text();
    }

    @Override
    public void setValue(String value) {
        SelenideElement input = element().parent().$(".n2o-input");
        input.setValue(value);
        input.pressEnter();
    }

    @Override
    public void shouldBeEmpty() {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(Condition.empty);
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value) {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.value(value));
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(value == null || value.isEmpty() ?
                Condition.empty : Condition.text(value));
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        Condition condition = Condition.attribute("placeholder", value);
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(condition);
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(condition);
    }

    @Override
    public void clickEyeButton() {
        element().parent().$(".n2o-input-password-toggler").hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldHaveVisiblePassword() {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(Condition.attribute("type", "text"));
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(Condition.attribute("type", "text"));
    }

    @Override
    public void shouldNotHaveVisiblePassword() {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(Condition.attribute("type", "password"));
        else element().$(".n2o-editable-cell .n2o-editable-cell-text").shouldHave(Condition.attribute("type", "password"));
    }

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-input");
    }
}
