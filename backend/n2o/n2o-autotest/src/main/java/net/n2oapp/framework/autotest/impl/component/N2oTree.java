package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.Tree;

public class N2oTree extends N2oComponent implements Tree {
    private static final String CSS_SELECTORS = ".n2o-rc-tree-treenode-switcher-open, .n2o-rc-tree-treenode-switcher-close";

    @Override
    public N2oTreeItem item(int index) {
        return new N2oTree.N2oTreeItem(element().$$(CSS_SELECTORS).get(index));
    }

    @Override
    public void shouldHaveItems(int size) {
        element().$$(CSS_SELECTORS).shouldHaveSize(size);
    }

    public class N2oTreeItem extends N2oComponent implements TreeItem {

        public N2oTreeItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveItem(String label) {
            element().$$(CSS_SELECTORS).find(Condition.text(label)).shouldBe(Condition.exist);
        }

        @Override
        public void expand() {
            if (!switcher().has(isExpanded()))
                switcher().click();
        }

        @Override
        public void collapse() {
            if (!switcher().has(isExpanded()))
                switcher().click();
        }

        @Override
        public void shouldBeExpanded() {
            switcher().shouldHave(Condition.cssClass("n2o-rc-tree-switcher_open"));
        }

        @Override
        public void shouldBeCollapsed() {
            switcher().shouldHave(Condition.cssClass("n2o-rc-tree-switcher_close"));
        }

        @Override
        public void shouldBeExpandable() {
            switcher().shouldBe(Condition.exist);
        }

        private Condition isExpanded() {
            return Condition.cssClass("n2o-rc-tree-switcher_open");
        }

        private SelenideElement switcher() {
            return element().parent().$(".n2o-rc-tree-switcher");
        }
    }
}
