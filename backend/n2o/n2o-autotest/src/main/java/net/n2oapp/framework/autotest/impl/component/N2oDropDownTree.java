package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import org.openqa.selenium.Keys;

public class N2oDropDownTree extends N2oComponent implements DropDownTree {
    private static final String CSS_SELECTORS = ".n2o-select-tree-tree-treenode-switcher-open, .n2o-select-tree-tree-treenode-switcher-close";

    @Override
    public DropDownTreeItem item(int index) {
        return new N2oDropDownTree.N2oDropDownTreeItem(element().$$(CSS_SELECTORS).get(index));
    }

    @Override
    public void shouldHaveItems(int size) {
        element().$$(CSS_SELECTORS).shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void clickOnSearchField() {
        searchField().click();
    }

    @Override
    public void setValue(String value) {
        searchField().setValue(value);
    }

    @Override
    public void clear() {
        searchField().clear();
    }

    private SelenideElement searchField() {
        return element().$(".n2o-select-tree-search__field");
    }

    public class N2oDropDownTreeItem extends N2oComponent implements DropDownTreeItem {

        public N2oDropDownTreeItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveOption(String label) {
            element().$$(CSS_SELECTORS).find(Condition.text(label)).shouldBe(Condition.exist);
        }

        @Override
        public void expand() {
            if (!switcher().has(isExpanded()))
                switcher().click();
        }

        @Override
        public void collapse() {
            if (switcher().has(isExpanded()))
                switcher().click();
        }

        @Override
        public void shouldBeExpanded() {
            switcher().shouldHave(Condition.cssClass("n2o-select-tree-tree-switcher_open"));
        }

        @Override
        public void shouldBeCollapsed() {
            switcher().shouldHave(Condition.cssClass("n2o-select-tree-tree-switcher_close"));
        }

        public void shouldHaveValue(String value) {
            element().shouldHave(Condition.text(value));
        }

        private Condition isExpanded() {
            return Condition.cssClass("n2o-select-tree-tree-switcher_open");
        }

        private SelenideElement switcher() {
            return element().parent().$(".n2o-select-tree-tree-switcher");
        }
    }
}
