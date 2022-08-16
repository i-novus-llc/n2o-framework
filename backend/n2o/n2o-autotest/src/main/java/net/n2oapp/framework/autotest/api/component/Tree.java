package net.n2oapp.framework.autotest.api.component;

public interface Tree extends Component {
    TreeItem item(int index);

    void shouldHaveItems(int size);

    interface TreeItem extends Expandable, Component, Badge {
        void shouldHaveItem(String label);
    }
}
