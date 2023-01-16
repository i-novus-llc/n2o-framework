package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;
import org.openqa.selenium.Keys;

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
    public void expandOptions() {
        element().hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void expandParentOptions(int parentId) {
        element().$$(".n2o-select-tree-dropdown [role=\"treeitem\"] i").get(parentId).click();
    }

    @Override
    public void setFilter(String value) {
        element().$(".n2o-select-tree-search__field").sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    @Override
    public void shouldDisplayedOptions(CollectionCondition condition) {
        element().$$(".n2o-select-tree-dropdown [role=\"treeitem\"]").shouldHave(condition);
    }

    @Override
    public void selectOption(int index) {
        element().$$(".n2o-select-tree-dropdown [role=\"treeitem\"]").get(index)
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeSelected(int index, String value) {
        element().$$(".n2o-select-tree-selection__choice").get(index).shouldHave(Condition.text(value));
    }

    @Override
    public void removeOption(int index) {
        element().$$(".n2o-select-tree-selection__choice .n2o-select-tree-selection__choice__remove").get(index)
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void removeAllOptions() {
        element().$(".n2o-select-tree-selection__clear")
                .hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldBeUnselected() {
        element().$$(".n2o-select-tree-selection__choice").shouldHaveSize(0);
    }
}
