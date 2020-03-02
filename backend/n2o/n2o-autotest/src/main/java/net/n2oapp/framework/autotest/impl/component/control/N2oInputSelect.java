package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.control.InputSelectControl;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Компонент поля ввода текста с выбором из выпадающего списка (input-select) для автотестирования
 */
public class N2oInputSelect extends N2oControl implements InputSelectControl {

    private boolean isMulti() {
        return element().$(".n2o-inp").has(Condition.cssClass("n2o-inp--multi"));
    }

    @Override
    public void val(String value) {
        element().$(".n2o-inp").sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
        element().click();
    }

    public void valMulti(String... values) {
        if (isMulti()) {
            Arrays.stream(values).forEach(s -> {
                element().$(".n2o-inp--multi").sendKeys(Keys.chord(Keys.CONTROL, "a"), s);
                element().$(".n2o-inp--multi").pressEnter();
            });
        }
    }

    @Override
    public void shouldHaveValue(String value) {
        element().$(".n2o-inp").shouldHave(Condition.value(value));
    }

    @Override
    public void select(int index) {
        element().click();
        element().parent().$$(".n2o-pop-up button")
                .shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index).click();
    }

    @Override
    public void select(Condition by) {
        element().parent().$$(".n2o-pop-up button").findBy(by).click();
    }


    @Override
    public void selectMulti(int... indexes) {
        if (isMulti()) {
            element().click();
            ElementsCollection elements = element().parent().$$(".n2o-pop-up button");
            IntStream.of(indexes).forEach(i -> elements.shouldBe(CollectionCondition.sizeGreaterThan(i)).get(i).click());
        }
    }

    @Override
    public void shouldSelected(String value) {
        element().$(".n2o-input-items .n2o-inp").shouldHave(Condition.value(value));
    }

    @Override
    public void shouldSelectedMulti(String... values) {
        if (isMulti()) {
            ElementsCollection selectedItems = element().$$(".n2o-input-select-selected-list .selected-item");
            selectedItems.shouldHaveSize(values.length);
            if (values.length != 0)
                selectedItems.shouldHave(CollectionCondition.textsInAnyOrder(values));
        }
    }

    @Override
    public void clear() {
        element().$(".n2o-input-clear").hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void clearItems(String... items) {
        if (isMulti()) {
            ElementsCollection selectedItems = element().$$(".n2o-input-select-selected-list .selected-item");
            Arrays.stream(items).forEach(s -> selectedItems.find(Condition.text(s)).$("button").click());
        }
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldHave(Condition.cssClass("disabled"));
    }
}
