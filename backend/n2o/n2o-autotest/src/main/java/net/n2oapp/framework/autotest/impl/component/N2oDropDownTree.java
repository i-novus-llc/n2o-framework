package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.DropDownTree;

public class N2oDropDownTree extends N2oComponent implements DropDownTree {
    private static final String CSS_SELECTORS = ".n2o-select-tree-tree-treenode-switcher-open, .n2o-select-tree-tree-treenode-switcher-close";

    @Override
    public DropDownTreeItem item(int index) {
        return new N2oDropDownTree.N2oDropDownTreeItem(element().$$(CSS_SELECTORS).get(index));
    }

    @Override
    public void shouldHaveItems(int size) {
        element().$$(CSS_SELECTORS).shouldHaveSize(size);
    }

    public class N2oDropDownTreeItem extends N2oComponent implements DropDownTreeItem {

        public N2oDropDownTreeItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveOption(String label) {
            element().$("n2o-select-tree-label").shouldHave(Condition.text(label));
        }

        @Override
        public void expand() {
            if (!expanded())
                switcher().click();
        }

        @Override
        public void collapse() {
            if (expanded())
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

        @Override
        public void shouldBeExpandable() {
            switcher().shouldBe(Condition.exist);
        }

        private boolean expanded() {
            return switcher().has(Condition.cssClass("n2o-select-tree-tree-switcher_open"));
        }

        private SelenideElement switcher() {
            return element().parent().$(".n2o-select-tree-tree-switcher");
        }
    }
}
