package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Arrays;

/**
 * Компонент ввода текста с выбором из выпадающего списка (input-select) для автотестирования
 */
public class N2oInputSelect extends N2oControl implements InputSelect {

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        should(Condition.attribute("value", value), inputElement(), duration);
    }

    @Override
    public void shouldBeEmpty() {
        selectedItemsContainer().shouldBe(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        inputElement().setValue(value);
    }

    @Override
    public void setMultiValue(String... values) {
        Arrays.stream(values).forEach(s -> inputElement().setValue(s).pressEnter());
    }

    @Override
    public void pressEnter() {
        inputElement().pressEnter();
    }

    @Override
    public void backspace() {
        inputElement().sendKeys(Keys.BACK_SPACE);
    }

    @Override
    public void click() {
        inputElement().click();
    }

    @Override
    public void shouldHaveDropdownMessage(String value, Duration... duration) {
        should(Condition.exactText(value), element().parent().parent().$(".n2o-dropdown-control"), duration);
    }

    @Override
    public void shouldSelectedMulti(String[] values, Duration... duration) {
        ElementsCollection selectedItemsValue = selectedItemsValue();
        should(CollectionCondition.size(values.length), selectedItemsValue, duration);
        should(CollectionCondition.texts(values), selectedItemsValue, duration);
    }

    @Override
    public void shouldSelectedMultiSize(int size) {
        selectedItemsValue().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void clearUsingIcon() {
        clearIcon().hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void clear() {
        inputElement().clear();
    }

    @Override
    public void clearItems(String... items) {
        ElementsCollection selectedItems = selectedItemsContainer().$$(".tags");
        Arrays.stream(items)
                .forEach(s -> selectedItems.find(Condition.text(s))
                        .$("button")
                        .click());
    }

    @Override
    public void shouldHaveDisabledItems(String... items) {
        ElementsCollection selectedItems = selectedItems();
        Arrays.stream(items)
                .filter(s -> selectedItems.find(Condition.text(s)).exists())
                .forEach(s -> selectedItems.find(Condition.text(s))
                        .$("button")
                        .shouldNotBe(Condition.exist));
    }

    @Override
    public void shouldBeDisabled() {
        inputElement().shouldBe(Condition.disabled);
    }

    @Override
    public void openPopup() {
        SelenideElement popupIcon = popupIcon();

        if (!popupIcon.is(Condition.cssClass("open")))
            popupIcon.click();
    }

    @Override
    public void closePopup() {
        SelenideElement popupIcon = popupIcon();

        if (popupIcon.is(Condition.cssClass("open")))
            popupIcon.click();
    }

    @Override
    public void shouldBeOpened() {
        selectPopUp().shouldNotBe(Condition.hidden);
    }

    @Override
    public void shouldBeClosed() {
        selectPopUp().shouldBe(Condition.hidden);
    }

    @Override
    public DropDown dropdown() {
        return N2oSelenide.component(element().parent().parent().$(".n2o-dropdown-control"), DropDown.class);
    }

    protected SelenideElement inputElement() {
        return element().parent().has(Condition.cssClass("multi"))
                ? element().$(".input-multiple")
                : element().$(".input-single");
    }

    protected SelenideElement selectPopUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }

    protected SelenideElement popupIcon() {
        return element().$(".select-controls");
    }

    protected SelenideElement clearIcon() {
        return element().$(".clear");
    }

    protected SelenideElement selectedItemsContainer() {
        return element().$(".input-tags");
    }

    protected ElementsCollection selectedItemsValue() {
        return element().$$(".input-tags .tag-value");
    }

    protected ElementsCollection selectedItems() {
        return element().$$(".selected-item");
    }
}
