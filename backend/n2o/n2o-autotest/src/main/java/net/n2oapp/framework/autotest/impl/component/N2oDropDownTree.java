package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.DropDownTree;

import java.time.Duration;

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
        searchField().sendKeys(value);
    }

    @Override
    public void shouldHaveOption(String label) {
        element().$$(CSS_SELECTORS).find(Condition.text(label)).shouldBe(Condition.exist);
    }

    @Override
    public void clear() {
        searchField().clear();
    }

    protected SelenideElement searchField() {
        return element().parent().$(".n2o-select-tree-selection-search-input");
    }

    public class N2oDropDownTreeItem extends N2oComponent implements DropDownTreeItem {

        private static final String SELECTED_NODE = "n2o-select-tree-tree-treenode-checkbox-checked";

        private static final String SWITCHER = "n2o-select-tree-tree-switcher";

        public N2oDropDownTreeItem(SelenideElement element) {
            setElement(element);
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
            switcher().shouldHave(Condition.cssClass(String.format("%s_open", SWITCHER)));
        }

        @Override
        public void shouldBeCollapsed() {
            switcher().shouldHave(Condition.cssClass(String.format("%s_close", SWITCHER)));
        }

        @Override
        public void click() {
            element().click();
        }

        public void shouldHaveValue(String value, Duration... duration) {
            should(Condition.text(value), duration);
        }

        public void shouldBeSelected() {
            element().shouldHave(Condition.cssClass(SELECTED_NODE));
        }

        public void shouldNotBeSelected() {
            element().shouldNotHave(Condition.cssClass(SELECTED_NODE));
        }

        private WebElementCondition isExpanded() {
            return Condition.cssClass(String.format("%s_open", SWITCHER));
        }

        protected SelenideElement switcher() {
            return element().$(String.format(".%s", SWITCHER));
        }
    }
}
