package net.n2oapp.framework.autotest.api.component;

public interface DropDown extends Component{

    DropDownItem item(int index);

    void shouldHaveItems(int size);

    interface DropDownItem extends Component, Badge{
    }
}
