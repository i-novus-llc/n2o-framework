package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.DropDown;

public class N2oDropDown extends N2oComponent implements DropDown {

    @Override
    public DropDownItem item(int index) {
        return new N2oDropDownItem(element().$$(".dropdown-item").get(index));
    }

    @Override
    public void shouldHaveItems(int size) {
        element().$$(".dropdown-item").shouldHave(CollectionCondition.size(size));
    }

    public class N2oDropDownItem extends N2oComponent  implements DropDownItem {
        public N2oDropDownItem(SelenideElement element) {
            setElement(element);
        }

        public void shouldHaveValue(String value) {
            element().shouldHave(Condition.text(value));
        }
    }
}
