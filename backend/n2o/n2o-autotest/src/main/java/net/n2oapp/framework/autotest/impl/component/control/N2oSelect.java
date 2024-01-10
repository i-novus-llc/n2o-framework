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
    public void shouldHaveValue(String value, Duration... duration) {
        shouldSelected(value, duration);
    }

    @Override
    public void shouldBeEmpty() {
        selectedItemsContainer().shouldBe(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        selectedItemsContainer().$(".input").setValue(value);
    }

    @Override
    public void click() {
        selectedItemsContainer().click();
    }

    @Override
    public void shouldSelected(String value, Duration... duration) {
        should(Condition.text(value), selectedItemsContainer(), duration);
    }

    @Override
    public void clear() {
        clearIcon().hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeCleanable() {
        clearIcon().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeCleanable() {
        clearIcon().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldHave(Condition.cssClass("disabled"));
    }

    @Override
    public void openPopup() {
        SelenideElement popupIcon = popupIcon();

        if (!popupIcon.is(Condition.cssClass("isExpanded")))
            popupIcon.click();
    }

    @Override
    public void closePopup() {
        SelenideElement popupIcon = popupIcon();

        if (popupIcon.is(Condition.cssClass("isExpanded")))
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
        return element().parent().parent().$(".n2o-select-pop-up");
    }

    protected SelenideElement popupIcon() {
        return element().$(".n2o-popup-control");
    }

    protected SelenideElement clearIcon() {
        return element().$(".n2o-input-clear");
    }

    protected SelenideElement selectedItemsContainer() {
        return element().$(".n2o-input-items");
    }
}
