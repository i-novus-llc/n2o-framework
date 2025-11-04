package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import org.openqa.selenium.Keys;

import java.time.Duration;

/**
 * Компонент ввода текста для автотестирования
 */
public class N2oInputText extends N2oControl implements InputText {

    @Override
    public void shouldBeEmpty() {
        SelenideElement input = inputElement();
        if (input.exists())
            input.shouldBe(Condition.empty);
        else
            editCellElement().shouldBe(Condition.empty);
    }

    @Override
    public String getValue() {
        SelenideElement input = inputElement();
        return input.exists() ? input.getValue() : editCellElement().text();
    }

    @Override
    public void setValue(String value) {
        if (editCellInputElement().exists()) {
            editCellInputElement().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
            editCellInputElement().pressEnter();
        } else
            inputElement().setValue(value);
    }

    @Override
    public void insert() {
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "v"));
    }

    @Override
    public void pressEnter() {
        inputElement().pressEnter();
    }

    @Override
    public void click() {
        inputElement().click();
    }

    @Override
    public void clear() {
        inputElement().clear();
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        SelenideElement input = inputElement();

        if (input.exists())
            should(Condition.exactValue(value), input, duration);
        else
            should(Condition.text(value), editCellElement(), duration);
    }

    @Override
    public void shouldHavePlaceholder(String placeholder) {
        WebElementCondition condition = Condition.attribute("placeholder", placeholder);
        SelenideElement input = inputElement();

        if (input.exists())
            input.shouldHave(condition);
        else
            editCellElement().shouldHave(condition);
    }

    @Override
    public void clickPlusStepButton() {
        stepButton().get(0).click();
    }

    @Override
    public void clickMinusStepButton() {
        stepButton().get(1).click();
    }

    @Override
    public void shouldHaveMeasure() {
        inputMeasure().should(Condition.exist);
    }

    @Override
    public void shouldHaveMeasureText(String text, Duration... duration) {
        should(Condition.text(text), inputMeasure(), duration);
    }

    protected ElementsCollection stepButton() {
        return element().parent().$$(".n2o-input-number-buttons button");
    }

    protected SelenideElement inputElement() {
        return element().shouldBe(Condition.exist).parent().$(".n2o-input");
    }

    protected SelenideElement editCellInputElement() {
        return element().$(".n2o-advanced-table-edit-control");
    }

    protected SelenideElement editCellElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }

    protected SelenideElement inputMeasure() {
        SelenideElement elm = element().parent();
        if (elm.is(Condition.cssClass("n2o-input-number")))
            elm = elm.parent();
        return elm.$(".n2o-control-container-placeholder");
    }
}
