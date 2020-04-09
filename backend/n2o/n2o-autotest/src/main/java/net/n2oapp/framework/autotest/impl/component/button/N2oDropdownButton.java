package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

public class N2oDropdownButton extends N2oButton implements DropdownButton {
    @Override
    public StandardButton menuItem(String label) {
        return N2oSelenide.component(element().parent().$$(".dropdown-item").findBy(Condition.text(label)), N2oStandardButton.class);
    }

    @Override
    public StandardButton menuItem(Condition by) {
        return N2oSelenide.component(element().parent().$$(".dropdown-item").findBy(by), N2oStandardButton.class);
    }
}
