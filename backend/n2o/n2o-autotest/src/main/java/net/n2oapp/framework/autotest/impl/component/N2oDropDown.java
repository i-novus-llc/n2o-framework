package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Badge;
import net.n2oapp.framework.autotest.api.component.DropDown;

public class N2oDropDown extends N2oComponent implements DropDown {

    @Override
    public DropDownItem item(int index) {
        return new N2oDropDownItem(element().$$(".dropdown-item").get(index));
    }

    @Override
    public void shouldHaveItem(int size) {
        element().$$(".dropdown-item").shouldHaveSize(size);
    }

    public class N2oDropDownItem extends N2oComponent  implements DropDownItem {

        public N2oDropDownItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveBadge() {
            element().$(".badge").shouldBe(Condition.exist);
        }

        @Override
        public Badge badge() {
            return N2oSelenide.component(element().$(".badge"), N2oBadge.class);
        }
    }
}
