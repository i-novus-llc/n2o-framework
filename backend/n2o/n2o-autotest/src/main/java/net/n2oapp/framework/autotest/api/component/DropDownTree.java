package net.n2oapp.framework.autotest.api.component;

public interface DropDownTree extends Component {
    DropDownTreeItem item(int index);

    void shouldHaveItems(int size);

    void setValue(String value);

    void clear();

    interface DropDownTreeItem extends Expandable, Component, DropDown.DropDownItem {
        void shouldHaveOption(String label);
    }
}
