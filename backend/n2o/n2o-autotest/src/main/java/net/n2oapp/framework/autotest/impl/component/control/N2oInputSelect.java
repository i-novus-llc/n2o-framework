package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Компонент ввода текста с выбором из выпадающего списка (input-select) для автотестирования
 */
public class N2oInputSelect extends N2oControl implements InputSelect {

    private boolean isMulti() {
        return input().has(Condition.cssClass("n2o-inp--multi"));
    }

    @Override
    public void click() {
        element().$(".n2o-input-items").click();
    }

    @Override
    public void val(String value) {
        input().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        element().click();
    }

    public void valMulti(String... values) {
        Arrays.stream(values).forEach(s -> {
            element().$(".n2o-inp--multi").sendKeys(Keys.chord(Keys.CONTROL, "a"), s);
            element().$(".n2o-inp--multi").pressEnter();
        });
    }

    @Override
    public void shouldHaveValue(String value) {
        input().shouldHave(Condition.value(value));
    }


    @Override
    public void shouldHaveOptions(String... options) {
        expand();
        selectPopUp().$$("button .text-cropped,.custom-control-label").shouldHave(CollectionCondition.exactTexts(options));
    }

    @Override
    public void select(int index) {
        expand();
        popUpButtons().shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index).click();
    }

    @Override
    public void select(Condition by) {
        expand();
        popUpButtons().findBy(by).click();
    }


    @Override
    public void selectMulti(int... indexes) {
        expand();
        IntStream.of(indexes).forEach(i -> popUpButtons().shouldBe(CollectionCondition.sizeGreaterThan(i)).get(i).click());
    }

    @Override
    public void shouldSelected(String value) {
        element().$(".n2o-input-items .n2o-inp").shouldHave(Condition.value(value));
    }

    @Override
    public void shouldBeEmpty() {
        element().$(".n2o-input-items .n2o-inp").shouldBe(Condition.empty);
        if (isMulti())
            input().parent().$(".selected-item").shouldNot(Condition.exist);
    }

    @Override
    public void shouldSelectedMulti(String... values) {
        ElementsCollection selectedItems = element().$$(".selected-item");
        selectedItems.shouldHaveSize(values.length);
        if (values.length != 0)
            selectedItems.shouldHave(CollectionCondition.textsInAnyOrder(values));
    }

    @Override
    public void itemShouldBeEnabled(Boolean enabled, String value) {
        element().click();
        if (enabled)
            popUpButtons().findBy(Condition.text(value))
                    .shouldNotHave(Condition.cssClass("disabled"));
        else
            popUpButtons().findBy(Condition.text(value))
                    .shouldHave(Condition.cssClass("disabled"));
    }

    @Override
    public void clear() {
        element().$(".n2o-input-clear").hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void clearItems(String... items) {
        ElementsCollection selectedItems = element().$$(".selected-item");
        Arrays.stream(items).forEach(s -> selectedItems.find(Condition.text(s)).$("button").click());
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldHave(Condition.cssClass("disabled"));
    }

    @Deprecated
    public void expand() {
        openPopup();
    }

    @Override
    public void openPopup() {
        SelenideElement elm = element().$(".n2o-popup-control");
        if (!elm.is(Condition.cssClass("isExpanded")))
            elm.click();
    }

    @Deprecated
    public void collapse() {
        closePopup();
    }

    @Override
    public void closePopup() {
        SelenideElement elm = element().$(".n2o-popup-control");
        if (elm.is(Condition.cssClass("isExpanded")))
            elm.click();
    }

    @Deprecated
    public void shouldBeExpanded() {
        shouldBeOpened();
    }

    @Override
    public void shouldBeOpened() {
        selectPopUp().shouldNotBe(Condition.hidden);
    }

    @Deprecated
    public void shouldBeCollapsed() {
        shouldBeClosed();
    }

    @Override
    public void shouldBeClosed() {
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

    @Override
    public void itemShouldHaveStatusColor(String value, Colors color) {
        element().click();
        popUpButtons().findBy(Condition.text(value))
                .$(".n2o-status-text_icon__right, .n2o-status-text_icon__left")
                .shouldHave(Condition.cssClass(color.name("bg-")));
    }

    @Override
    public DropDown dropdown() {
        return N2oSelenide.component(element().parent().parent().$(".n2o-dropdown-control"), DropDown.class);
    }

    private SelenideElement input() {
        return element().$(".n2o-inp");
    }

    private SelenideElement selectPopUp() {
        return element().parent().parent().$(".n2o-pop-up");
    }

    private ElementsCollection popUpButtons() {
        return selectPopUp().$$("button");
    }

}
