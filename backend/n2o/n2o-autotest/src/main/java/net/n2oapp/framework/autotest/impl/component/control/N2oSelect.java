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
    public void openOptions() {
        element().$(".n2o-input-control .n2o-popup-control").click();
    }

    @Override
    public void closeOptions() {

    }

    @Override
    public void find(String query) {
        element().$(".n2o-input-items input").sendKeys(query, Keys.ARROW_DOWN);
    }

    @Override
    public void select(int index) {
        element().click();
        selectPopUp().$$("button").shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index).click();
    }

    @Override
    public void select(Condition by) {
        element().parent().$$(".n2o-pop-up button").findBy(by).click();
    }

    @Override
    public void selectMulti(int... indexes) {
        if (element().$(".n2o-popup-control.isExpanded").is(Condition.not(Condition.exist)))
            element().click();
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
        element().$(".form-control").shouldHave(Condition.cssClass("disabled"));
    }

    private SelenideElement selectPopUp() {
        return element().parent().parent().$(".n2o-select-pop-up");
    }
}
