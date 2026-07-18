package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.Select;

import java.time.Duration;

/**
 * Компонент выбора из выпадающего списка для автотестирования
 */
public class N2oSelect extends N2oControl implements Select {

    @Override
    public void shouldHavePlaceholder(String value) {
        selectedItem().shouldHave(Condition.attribute("placeholder", value));
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        shouldSelected(value, duration);
    }

    @Override
    public void shouldBeEmpty() {
        selectedItemsContainer().shouldBe(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        selectedItem().$(".input").setValue(value);
    }

    @Override
    public void pressEnter() {
        selectedItem().pressEnter();
    }

    @Override
    public void click() {
        selectedItem().click();
    }

    @Override
    public void shouldSelected(String value, Duration... duration) {
        if (selectedItem().exists()) {
            should(Condition.attribute("value", value), selectedItem(), duration);
        } else {
            should(Condition.text(value), selectedItemsContainer(), duration);
        }
    }

    @Override
    public void shouldSelected(int count, Duration... duration) {
        should(Condition.text(String.format("Выбрано: %d", count)), selectedItemsContainer(), duration);
    }

    @Override
    public void clear() {
        clearIcon().hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeCleanable() {
        clearIcon().parent().shouldNotHave(Condition.cssClass("read-only"));
    }

    @Override
    public void shouldNotBeCleanable() {
        clearIcon().parent().shouldHave(Condition.cssClass("read-only"));
    }

    @Override
    public void shouldBeDisabled() {
        selectedItem().shouldBe(Condition.disabled);
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

    protected SelenideElement selectedItem() {
        return element().$(".input-single");
    }
}
