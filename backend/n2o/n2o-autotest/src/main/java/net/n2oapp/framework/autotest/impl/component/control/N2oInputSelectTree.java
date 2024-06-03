package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.*;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;

import java.time.Duration;

/**
 * Компонент ввода с выбором в выпадающем списке в виде дерева для автотестирования
 */
public class N2oInputSelectTree extends N2oControl implements InputSelectTree {

    private final String OPTION_LOCATOR = ".n2o-select-tree-tree-list .n2o-select-tree-tree-treenode";

    @Override
    public void shouldBeEmpty() {

    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {

    }

    @Override
    public void shouldHavePlaceholder(String value) {
        element().$(".n2o-select-tree-selection-placeholder").shouldHave(Condition.text(value));
    }

    @Override
    public void click() {
        element().hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void expandParentOptions(int parentIndex) {
        dropdownElement().$$(String.format("%s i", OPTION_LOCATOR)).get(parentIndex).click();
    }

    @Override
    public void setFilter(String value) {
        input().setValue(value);
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
    public void clearSearchField() {
        input().clear();
    }

    @Override
    public void shouldDisplayedOptions(WebElementsCondition condition) {
        dropdownElement().$$(OPTION_LOCATOR).shouldHave(condition);
    }

    @Override
    public void selectOption(int index) {
        dropdownElement().$$(OPTION_LOCATOR).get(index)
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeSelected(int index, String value, Duration... duration) {
        should(Condition.text(value), selectedElements().get(index), duration);
    }

    @Override
    public void removeOption(int index) {
        element().$$(".n2o-select-tree-selection-overflow-item").get(index).$(".n2o-select-tree-selection-item-remove")
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void removeAllOptions() {
        element().$(".n2o-select-tree-clear")
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeUnselected() {
        selectedElements().shouldHave(CollectionCondition.size(0));
    }

    @Override
    public DropDownTree dropdown() {
        return N2oSelenide.component(dropdownElement(), DropDownTree.class);
    }

    protected SelenideElement dropdownElement() {
        return element().$(".n2o-select-tree-dropdown");
    }

    @Override
    public void openPopup() {
        if (!isOpened())
            element().click();
    }

    @Override
    public void closePopup() {
        if (isOpened())
            element().click();
    }

    @Override
    public void shouldBeOpened() {
        dropdownElement().shouldBe(Condition.visible);
    }

    @Override
    public void shouldBeClosed() {
        dropdownElement().shouldNotBe(Condition.visible);
    }

    @Override
    public void shouldBeDisabled() {
        input().shouldBe(Condition.disabled);
    }

    protected SelenideElement switcher() {
        return element().$(".n2o-select-tree-arrow");
    }

    protected SelenideElement input() {
        return element().$(".n2o-select-tree-selection-search-input");
    }

    protected ElementsCollection selectedElements() {
        return element().$$(".n2o-select-tree-tree-treenode-checkbox-checked");
    }

    private boolean isOpened() {
        return element().has(Condition.cssClass(".n2o-select-tree-open"));
    }

    protected ElementsCollection selectedItems() {
        return element().$$(".n2o-select-tree-selection-item");
    }
}
