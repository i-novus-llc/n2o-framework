package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.Select;

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
    public void shouldSelected(String value) {
        selectedItemsContainer().shouldHave(Condition.text(value));
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

    @Deprecated
    public void shouldHaveOptions(String... options) {
        dropdown().shouldHaveOptions(options);
    }

    @Deprecated
    public void select(int index) {
        dropdown().selectItem(index);
    }

    @Deprecated
    public void select(Condition by) {
        dropdown().selectItemBy(by);
    }

    @Deprecated
    public void selectMulti(int... indexes) {
        if (!popupIcon().has(Condition.cssClass("isExpanded")))
            openPopup();

        dropdown().selectMulti(indexes);
    }

    @Deprecated
    public void shouldBeChecked(int... indexes) {
        dropdown().shouldBeChecked(indexes);
    }

    @Deprecated
    public void shouldNotBeChecked(int... indexes) {
        dropdown().shouldNotBeChecked(indexes);
    }

    @Deprecated
    public void optionShouldHaveDescription(String option, String description) {
        dropdown().optionShouldHaveDescription(option, description);
    }

    @Deprecated
    public void expand() {
        openPopup();
    }

    @Deprecated
    public void collapse() {
        closePopup();
    }

    @Deprecated
    public void shouldBeExpanded() {
        shouldBeOpened();
    }

    @Deprecated
    public void shouldBeCollapsed() {
        shouldBeClosed();
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
