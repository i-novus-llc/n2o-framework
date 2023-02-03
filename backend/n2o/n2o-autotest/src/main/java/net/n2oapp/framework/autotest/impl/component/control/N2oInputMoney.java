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
    public String val() {
        SelenideElement elm = inputElement();
        return elm.exists() ? elm.val() : element().$(".n2o-editable-cell .n2o-editable-cell-text").text();
    }

    @Override
    public void val(String value) {
        element().parent().$(".n2o-input-money").setValue(value);
        element().parent().$(".n2o-input-money").pressEnter();
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

    private SelenideElement inputElement() {
        element().shouldBe(Condition.exist);
        return element().parent().$(".n2o-input-money");
    }
}
