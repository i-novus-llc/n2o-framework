package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

public class N2oDropdownButton extends N2oButton implements DropdownButton {
    @Override
    public void shouldHaveItems(int count) {
        menuItems().shouldHaveSize(count);
    }

    @Override
    public StandardButton menuItem(String label) {
        return N2oSelenide.component(menuItems().findBy(Condition.text(label)), N2oStandardButton.class);
    }

    @Override
    public StandardButton menuItem(Condition by) {
        return N2oSelenide.component(element().parent().$$("div.dropdown-menu  .btn btn-secondary").findBy(by), N2oStandardButton.class);
    }

    @Override
    public void shouldBeVisible() {
        element().parent().shouldHave(Condition.cssClass("visible"));
    }

    @Override
    public void shouldNotBeVisible() {
        element().parent().shouldNotHave(Condition.cssClass("visible"));
    }

    private ElementsCollection menuItems() {
        return element().parent().$$("div.dropdown-menu .btn.btn-secondary,.dropdown-item");
    }
}
