package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.*;
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
    public void click() {
        element().$(".n2o-input-items").click();
    }

    @Override
    public void setValue(String value) {
        input().setValue(value);
    }

    @Override
    public void setMultiValue(String... values) {
        Arrays.stream(values).forEach(s -> input().setValue(s).pressEnter());
    }

    @Override
    public void pressEnter() {
        input().pressEnter();
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        if (input().exists())
            should(Condition.value(value), input(), duration);
        else
            should(Condition.text(value), cellInputElement(), duration);
    }

    @Override
    public void shouldHaveDropdownMessage(String value, Duration... duration) {
        should(Condition.exactText(value), element().parent().parent().parent().$(".n2o-dropdown-control"), duration);
    }

    protected SelenideElement cellInputElement() {
        return element().$(".n2o-editable-cell .n2o-editable-cell-text");
    }
    @Override
    public void shouldSelectedMulti(String[] values, Duration... duration) {
        if (values.length != 0) {
            should(CollectionCondition.size(values.length), selectedItems(), duration);
            should(CollectionCondition.textsInAnyOrder(values), selectedItems(), duration);
        }
    }

    @Override
    public void shouldSelectedMultiSize(int size) {
        selectedItems().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void clearUsingIcon() {
        element().$(".n2o-input-clear").hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void clear() {
        input().clear();
    }

    @Override
    public void shouldBeEmpty() {
        input().shouldBe(Condition.empty);
        if (isMulti())
            input().parent().$(".selected-item").shouldNot(Condition.exist);
    }

    @Override
    public void clearItems(String... items) {
        ElementsCollection selectedItems = selectedItems();
        Arrays.stream(items)
                .forEach(s -> selectedItems.find(Condition.text(s))
                        .$("button")
                        .click());
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
        return N2oSelenide.component(element().parent()
                .parent().$(".n2o-dropdown-control"), DropDown.class);
    }

    @Override
    public void backspace() {
        input().sendKeys(Keys.BACK_SPACE);
    }

    protected SelenideElement popupIcon() {
        return element().$(".n2o-popup-control");
    }

    protected ElementsCollection selectedItems() {
        return element().$$(".selected-item");
    }

    protected SelenideElement input() {
        return element().$(".n2o-inp");
    }

    protected SelenideElement selectPopUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }

    private boolean isMulti() {
        return input().has(Condition.cssClass("n2o-inp--multi"));
    }
}
