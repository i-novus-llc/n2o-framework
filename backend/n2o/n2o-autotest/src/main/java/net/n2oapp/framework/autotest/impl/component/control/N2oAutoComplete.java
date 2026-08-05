package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.AutoComplete;
import org.openqa.selenium.Keys;

import java.time.Duration;


/**
 * Компонент ввода текста с автозаполнением для автотестирования
 */
public class N2oAutoComplete extends N2oControl implements AutoComplete {

    @Override
    public void shouldBeEmpty() {
        inputElement().shouldBe(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        inputElement().setValue(value);
    }

    @Override
    public void pasteValue() {
        inputElement().sendKeys(Keys.chord(Keys.CONTROL, "v"));
    }

    @Override
    public void click() {
        element().click();
    }

    @Override
    public void clear() {
        inputElement().clear();
    }

    @Override
    public void enter() {
        inputElement().sendKeys(Keys.ENTER);
    }

    @Override
    public void removeTag(String value) {
        selectedItems().findBy(Condition.text(value)).$("button").click();
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        should(Condition.value(value), inputElement(), duration);
    }

    @Override
    public void shouldHaveTags(String[] tags, Duration... duration) {
        should(CollectionCondition.size(tags.length), selectedItems(), duration);
        should(CollectionCondition.texts(tags), selectedItems(), duration);
    }

    @Override
    public void shouldHaveDropdownOptions(String[] values, Duration... duration) {
        should(CollectionCondition.texts(values), dropdownOptions(), duration);
    }

    @Override
    public void shouldNotHaveDropdownOptions() {
        dropdownOptions().shouldHave(CollectionCondition.size(0));
    }

    @Override
    public void chooseDropdownOption(String value) {
        dropdownOptions().find(Condition.text(value))
                .shouldBe(Condition.exist)
                .click();
    }

    @Override
    public void shouldBeDisabled() {
        inputElement().shouldBe(Condition.disabled);
    }

    protected SelenideElement inputElement() {
        return element().$(".n2o-inp");
    }

    protected ElementsCollection selectedItems() {
        return element().$$(".selected-item");
    }

    protected ElementsCollection dropdownOptions() {
        return element().parent().$$(".n2o-dropdown-control button");
    }

    @Override
    public void openPopup() {

    }

    @Override
    public void closePopup() {

    }

    @Override
    public void shouldBeOpened() {
        selectPopUp().shouldNotBe(Condition.hidden);
    }

    @Override
    public void shouldBeClosed() {
        selectPopUp().shouldBe(Condition.hidden);
    }

    protected SelenideElement selectPopUp() {
        return element().parent().$(".n2o-pop-up");
    }
}
