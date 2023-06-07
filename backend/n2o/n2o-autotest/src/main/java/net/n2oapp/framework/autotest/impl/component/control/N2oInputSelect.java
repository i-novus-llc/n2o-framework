package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;

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
    public void shouldHaveValue(String value) {
        input().shouldHave(Condition.value(value));
    }

    @Override
    public void shouldSelectedMulti(String... values) {
        if (values.length != 0)
            selectedItems().shouldHave(CollectionCondition.size(values.length),
                    CollectionCondition.textsInAnyOrder(values));
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
