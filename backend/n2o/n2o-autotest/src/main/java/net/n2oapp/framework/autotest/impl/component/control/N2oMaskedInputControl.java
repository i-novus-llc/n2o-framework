package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.MaskedInputControl;
import org.openqa.selenium.Keys;

/**
 * Ввод текста для автотестирования
 */
public class N2oMaskedInputControl extends N2oControl implements MaskedInputControl {

    @Override
    public void shouldBeEmpty() {
        SelenideElement elm = inputElement();
        if (elm.exists()) inputElement().shouldBe(Condition.empty);
        else cellInputElement().shouldBe(Condition.empty);
    }

    @Override
    public String val() {
        SelenideElement elm = inputElement();
        return elm.exists() ? elm.val() : cellInputElement().text();
    }

    @Override
    public void val(String value) {
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        inputElement().pressEnter();
    }

    @Override
    public void shouldHaveValue(String value) {
        SelenideElement elm = inputElement();
        if (elm.exists()) elm.shouldHave(Condition.value(value));
        else cellInputElement().shouldHave(Condition.text(value));
    }

    private SelenideElement inputElement() {
        return element().parent().$(".n2o-input-mask");
    }

    private SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }
}
