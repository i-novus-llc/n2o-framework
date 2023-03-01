package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;
import org.openqa.selenium.Keys;

import java.util.Objects;

/**
 * Компонент ввода с выбором в выпадающем списке в виде дерева для автотестирования
 */
public class N2oInputSelectTree extends N2oControl implements InputSelectTree {

    @Override
    public void shouldBeEmpty() {

    }

    @Override
    public void shouldHaveValue(String value) {

    }

    @Override
    public void shouldHavePlaceholder(String value) {
        element().$(".n2o-select-tree-search__field__placeholder").shouldHave(Condition.text(value));
    }

    @Override
    public void click() {
        element().hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void expandParentOptions(int parentIndex) {
        dropdownElement().$$("[role=\"treeitem\"] i").get(parentIndex).click();
    }

    @Override
    public void setFilter(String value) {
        input().setValue(value);
    }

    @Override
    public void clearSearchField() {
        input().clear();
    }

    @Override
    public void shouldDisplayedOptions(CollectionCondition condition) {
        dropdownElement().$$("[role=\"treeitem\"]").shouldHave(condition);
    }

    @Override
    public void selectOption(int index) {
        dropdownElement().$$("[role=\"treeitem\"]").get(index)
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeSelected(int index, String value) {
        selectedElements().get(index).shouldHave(Condition.text(value));
    }

    @Override
    public void removeOption(int index) {
        selectedElements().get(index).$(".n2o-select-tree-selection__choice__remove")
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void removeAllOptions() {
        element().$(".n2o-select-tree-selection__clear")
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
            switcher().click();
    }

    @Override
    public void closePopup() {
        if (isOpened())
            switcher().click();
    }

    @Override
    public void shouldBeOpened() {
        element().shouldHave(Condition.attribute("aria-expanded", "true"));
    }

    @Override
    public void shouldBeClosed() {
        element().shouldHave(Condition.attribute("aria-expanded", "false"));
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

    protected SelenideElement switcher() {
        return element().$(".n2o-select-tree-arrow");
    }

    protected SelenideElement input() {
        return element().$(".n2o-select-tree-search__field");
    }

    protected ElementsCollection selectedElements() {
        return element().$$(".n2o-select-tree-selection__choice");
    }

    private boolean isOpened() {
        return Objects.equals(element().getAttribute("aria-expanded"), "true");
    }
}
