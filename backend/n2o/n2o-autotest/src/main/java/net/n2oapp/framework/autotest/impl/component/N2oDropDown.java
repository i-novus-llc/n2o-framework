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
        return new N2oDropDownItem(element().$$(".dropdown-item").get(index));
    }

    @Override
    public void shouldHaveOptions(String... options) {
        element().$$("button .text-cropped,.custom-control-label").shouldHave(CollectionCondition.exactTexts(options));
    }

    public void selectItem(int index) {
        element().$$("button").shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index).click();
    }

    public void selectItemBy(Condition by) {
        element().$$("button").findBy(by).click();
    }

    public void selectMulti(int... indexes) {
        OptionalInt maxIndex = Arrays.stream(indexes).max();
        if (maxIndex.isPresent()) {
            ElementsCollection checkBoxes = element().$$("button")
                    .shouldBe(CollectionCondition.sizeGreaterThan(maxIndex.getAsInt()));

            Arrays.stream(indexes).forEach(index -> checkBoxes.get(index).click());
        }
    }

    public void shouldBeChecked(int... indexes) {
        OptionalInt maxIndex = Arrays.stream(indexes).max();
        if (maxIndex.isPresent()) {
            ElementsCollection checkBoxes = element().$$(".n2o-input")
                    .shouldBe(CollectionCondition.sizeGreaterThan(maxIndex.getAsInt()));

            Arrays.stream(indexes).forEach(index -> checkBoxes.get(index).shouldBe(Condition.checked));
        }
    }

    public void shouldNotBeChecked(int... indexes) {
        ElementsCollection checkBoxes = element().$$(".n2o-input")
                .shouldBe(CollectionCondition.sizeGreaterThan(Arrays.stream(indexes).max().getAsInt()));

        Arrays.stream(indexes).forEach(index -> checkBoxes.get(index).shouldNotBe(Condition.checked));
    }

    public void optionShouldHaveDescription(String item, String description) {
        SelenideElement elm = element().$$("button .text-cropped,.custom-control-label")
                .findBy(Condition.text(item)).parent();
        if (elm.is(Condition.cssClass("custom-checkbox")))
            elm = elm.parent();
        elm.$(".dropdown-header").shouldHave(Condition.text(description));
    }

    public void optionShouldHaveStatusColor(String option, Colors color) {
        element().$$("button").findBy(Condition.text(option))
                .$(".n2o-status-text_icon__right, .n2o-status-text_icon__left")
                .shouldHave(Condition.cssClass(color.name("bg-")));
    }

    @Override
    public void shouldHaveOptions(int size) {
        element().$$(".dropdown-item").shouldHave(CollectionCondition.size(size));
    }

    public void optionShouldBeEnabled(String option) {
        element().$$("button").findBy(Condition.text(option)).shouldBe(Condition.enabled);
    }

    public void optionShouldBeDisabled(String option) {
        element().$$("button").findBy(Condition.text(option)).shouldBe(Condition.disabled);
    }

    public class N2oDropDownItem extends N2oComponent  implements DropDownItem {
        public N2oDropDownItem(SelenideElement element) {
            setElement(element);
        }

        public void shouldHaveValue(String value) {
            element().shouldHave(Condition.text(value));
        }
    }
}
