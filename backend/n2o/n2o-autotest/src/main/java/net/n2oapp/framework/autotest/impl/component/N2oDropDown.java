package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.DropDown;

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
    public void shouldHaveOptions(String... options) {
        if (options.length > 0) {
            items().shouldBe(
                    CollectionCondition.sizeGreaterThan(options.length - 1)
            );
            element().$$(".dropdown-item .text-cropped, .dropdown-item .custom-control-label")
                    .shouldHave(CollectionCondition.exactTexts(options));
        }
    }

    public void selectItem(int index) {
        items().shouldBe(CollectionCondition.sizeGreaterThan(index))
                .get(index)
                .click();
    }

    public void selectItemBy(Condition by) {
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

    private ElementsCollection items() {
        return element().$$(".dropdown-item");
    }

    private SelenideElement itemByIndex(int index) {
        return items().get(index);
    }

    private SelenideElement itemByLabel(String label) {
        return items().findBy(Condition.text(label));
    }

    public class N2oDropDownItem extends N2oComponent  implements DropDownItem {

        public N2oDropDownItem(SelenideElement element) {
            setElement(element);
        }

        public void shouldHaveValue(String value) {
            element().shouldHave(Condition.text(value));
        }

        @Override
        public void shouldBeSelected() {
            element().shouldHave(Condition.cssClass("disabled"));
        }

        @Override
        public void shouldNotBeSelected() {
            element().shouldNotHave(Condition.cssClass("disabled"));
        }

        @Override
        public void shouldHaveDescription(String description) {
            element().$(".dropdown-header").shouldHave(Condition.text(description));
        }

        @Override
        public void shouldHaveStatusColor(Colors color) {
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
