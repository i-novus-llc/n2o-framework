package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.Select;
import org.openqa.selenium.Keys;

/**
 * Компонент выбора из выпадающего списка для автотестирования
 */
public class N2oSelect extends N2oControl implements Select {

    @Override
    public void shouldHaveValue(String value) {
        shouldSelected(value);
    }

    @Override
    public void shouldBeEmpty() {
        element().$(".n2o-input-items").shouldBe(Condition.empty);
    }

    @Override
    public void find(String query) {
        element().$(".n2o-input-items input").sendKeys(query, Keys.ARROW_DOWN);
    }

    @Override
    public void click() {
        element().$(".n2o-input-items").click();
    }

    @Override
    public void shouldHaveOptions(String... options) {
        expand();
        selectPopUp().$$("button .text-cropped,.custom-control-label").shouldHave(CollectionCondition.exactTexts(options));
    }

    @Override
    public void select(int index) {
        expand();
        selectPopUp().$$("button").shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index).click();
    }

    @Override
    public void select(Condition by) {
        expand();
        selectPopUp().$$("button").findBy(by).click();
    }

    @Override
    public void selectMulti(int... indexes) {
        if (element().$(".n2o-popup-control.isExpanded").is(Condition.not(Condition.exist)))
            expand();
        for (int index : indexes)
            selectPopUp().$$(".n2o-input").shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index).click();
    }

    @Override
    public void shouldSelected(String value) {
        element().$(".n2o-input-items").shouldHave(Condition.text(value));
    }

    @Override
    public void shouldBeChecked(int... indexes) {
        for (int index : indexes)
            selectPopUp().$$(".n2o-input").shouldBe(CollectionCondition.sizeGreaterThan(index))
                    .get(index).shouldBe(Condition.checked);
    }

    @Override
    public void shouldNotBeChecked(int... indexes) {
        for (int index : indexes)
            selectPopUp().$$(".n2o-input").shouldBe(CollectionCondition.sizeGreaterThan(index))
                    .get(index).shouldNotBe(Condition.checked);
    }

    @Override
    public void clear() {
        element().$(".n2o-input-clear").hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeCleanable() {
        element().$$(".n2o-input-clear").shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Override
    public void shouldNotBeCleanable() {
        element().$$(".n2o-input-clear").shouldHave(CollectionCondition.size(0));
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldHave(Condition.cssClass("disabled"));
    }

    @Override
    public void expand() {
        SelenideElement elm = element().$(".n2o-popup-control");
        if (!elm.is(Condition.cssClass("isExpanded")))
            elm.click();
    }

    @Override
    public void collapse() {
        SelenideElement elm = element().$(".n2o-popup-control");
        if (elm.is(Condition.cssClass("isExpanded")))
            elm.click();
    }

    @Override
    public void shouldBeExpanded() {
        selectPopUp().shouldNotBe(Condition.hidden);
    }

    @Override
    public void shouldBeCollapsed() {
        selectPopUp().shouldBe(Condition.hidden);
    }

    @Override
    public void optionShouldHaveDescription(String option, String description) {
        expand();
        SelenideElement elm = selectPopUp().$$("button .text-cropped,.custom-control-label")
                .findBy(Condition.text(option)).parent();
        if (elm.is(Condition.cssClass("custom-checkbox")))
            elm = elm.parent();
        elm.$(".dropdown-header").shouldHave(Condition.text(description));
    }

    private SelenideElement selectPopUp() {
        return element().parent().parent().$(".n2o-select-pop-up");
    }
}
