package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.*;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.DropDown;

import java.time.Duration;
import java.util.Arrays;
import java.util.OptionalInt;

public class N2oDropDown extends N2oComponent implements DropDown {

    @Override
    public DropDownItem item(int index) {
        return new N2oDropDownItem(itemByIndex(index));
    }

    @Override
    public DropDownItem item(String label) {
        return new N2oDropDownItem(itemByLabel(label));
    }

    @Override
    public void shouldHaveOptions(String[] options, Duration... duration) {
        if (options.length > 0) {
            items().shouldBe(
                    CollectionCondition.sizeGreaterThan(options.length - 1)
            );
            should(
                    CollectionCondition.exactTexts(options),
                    element().$$(".dropdown-item .text-cropped, .dropdown-item .custom-control-label"),
                    duration
            );
        }
    }

    public void selectItem(int index) {
        items().shouldBe(CollectionCondition.sizeGreaterThan(index))
                .get(index)
                .click();
    }

    public void selectItemBy(WebElementCondition by) {
        items().findBy(by).click();
    }

    public void selectMulti(int... indexes) {
        OptionalInt maxIndex = Arrays.stream(indexes).max();
        ElementsCollection items = items();

        if (maxIndex.isPresent()) {
            items().shouldBe(
                    CollectionCondition.sizeGreaterThan(maxIndex.getAsInt())
            );
            Arrays.stream(indexes).forEach(
                    index -> items.get(index).click()
            );
        }
    }

    public void shouldBeChecked(int... indexes) {
        OptionalInt maxIndex = Arrays.stream(indexes).max();
        ElementsCollection items = items();

        if (maxIndex.isPresent()) {
            Arrays.stream(indexes).forEach(
                    index -> items.get(index).$(".n2o-input").shouldBe(Condition.checked)
            );
        }
    }

    public void shouldNotBeChecked(int... indexes) {
        OptionalInt maxIndex = Arrays.stream(indexes).max();
        ElementsCollection items = items();

        if (maxIndex.isPresent()) {
            Arrays.stream(indexes).forEach(
                    index -> items.get(index).$(".n2o-input").shouldNotBe(Condition.checked)
            );
        }
    }

    @Override
    public void shouldHaveOptions(int size) {
        items().shouldHave(CollectionCondition.size(size));
    }
    @Override
    public void scrollDown() {
        Selenide.executeJavaScript("arguments[0].scrollTop = arguments[0].scrollHeight", element().getWrappedElement());
    }

    private ElementsCollection items() {
        return element().$$(".dropdown-item");
    }

    private SelenideElement itemByIndex(int index) {
        return items().get(index);
    }

    private SelenideElement itemByLabel(String label) {
        return items().findBy(Condition.text(label));
    }

    public class N2oDropDownItem extends N2oComponent implements DropDownItem {

        public N2oDropDownItem(SelenideElement element) {
            setElement(element);
        }

        public void shouldHaveValue(String value, Duration... duration) {
            should(Condition.text(value), duration);
        }

        @Override
        public void shouldBeSelected() {
            element().shouldHave(Condition.cssClass("selected"));
        }

        @Override
        public void shouldNotBeSelected() {
            element().shouldNotHave(Condition.cssClass("selected"));
        }

        @Override
        public void shouldHaveDescription(String description, Duration... duration) {
            should(Condition.text(description), element().$(".dropdown-header"), duration);
        }

        @Override
        public void shouldHaveStatusColor(ColorsEnum color) {
            element().$(".n2o-status-text_icon__right, .n2o-status-text_icon__left")
                    .shouldHave(Condition.cssClass(color.name("bg-")));
        }

        @Override
        public void shouldBeEnabled() {
            element().shouldBe(Condition.enabled);
        }

        @Override
        public void shouldBeDisabled() {
            element().shouldBe(Condition.disabled);
        }
    }
}
