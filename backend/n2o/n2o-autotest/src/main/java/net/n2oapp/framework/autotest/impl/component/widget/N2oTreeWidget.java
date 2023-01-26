package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.TreeWidget;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oTreeWidget extends N2oStandardWidget implements TreeWidget {
    protected String treeItem = ".n2o-rc-tree-treenode-switcher-open, .n2o-rc-tree-treenode-switcher-close";

    @Override
    public N2oTreeWidget.N2oTreeItem item(int index) {
        return new N2oTreeWidget.N2oTreeItem(element().$$(treeItem).get(index));
    }

    @Override
    public void shouldHaveItems(int size) {
        element().$$(treeItem).shouldHave(CollectionCondition.size(size));
    }

    public class N2oTreeItem extends N2oComponent implements TreeWidget.TreeItem {

        public N2oTreeItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveItem(String label) {
            element().$$(treeItem).find(Condition.text(label)).shouldBe(Condition.exist);
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

        private Condition isExpanded() {
            return Condition.cssClass("n2o-rc-tree-switcher_open");
        }

        private SelenideElement switcher() {
            return element().parent().$(".n2o-rc-tree-switcher");
        }
    }
}
