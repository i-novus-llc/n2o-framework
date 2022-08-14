package net.n2oapp.framework.autotest.api.component;

public interface DropDown extends Component{

    DropDownItem item(int index);

    void shouldHaveItem(int size);

    interface DropDownItem extends Component{
        void shouldHaveBadge();

        Badge badge();
    }
}
