package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
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
        //ToDo реализовать
    }

    @Override
    public void shouldHaveValue(String value) {
        //ToDo реализовать
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
    public void expandParentOptions(int parentIndex) {
        element().$$(".n2o-select-tree-dropdown [role=\"treeitem\"] i").get(parentIndex).click();
    }

    @Override
    public void setFilter(String value) {
        //ToDo: заменить на setValue selenide
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
        element().$$(".n2o-select-tree-selection__choice").shouldHave(CollectionCondition.size(0));
    }

    @Override
    public DropDownTree dropdown() {
        return N2oSelenide.component(element().$(".n2o-select-tree-dropdown"), DropDownTree.class);
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

    private boolean isOpened() {
        return Objects.equals(element().getAttribute("aria-expanded"), "true");
    }

    private SelenideElement switcher() {
        return element().$(".n2o-select-tree-arrow");
    }
}
