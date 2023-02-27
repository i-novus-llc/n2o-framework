package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

public class N2oDropdownButton extends N2oButton implements DropdownButton {

    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldHaveItems(int count) {
        menuItems().shouldHave(CollectionCondition.size(count));
    }

    @Override
    public StandardButton menuItem(String label) {
        return N2oSelenide.component(menuItems().findBy(Condition.text(label)), N2oStandardButton.class);
    }

    @Override
    public StandardButton menuItem(Condition by) {
        return N2oSelenide.component(element()
                .parent()
                .$$("div.dropdown-menu  .btn .btn-secondary")
                .findBy(by), N2oStandardButton.class);
    }

    @Override
    public void shouldBeVisible() {
        //ToDo: почему не Condition.visible?
        element().parent().shouldHave(Condition.cssClass("visible"));
    }

    @Override
    public void shouldBeHidden() {
        element().parent().shouldNotHave(Condition.cssClass("visible"));
    }

    @Deprecated
    public void shouldNotBeVisible() {
        shouldBeHidden();
    }

    @Override
    public void shouldBeExpanded() {
        //ToDo: можно ли заменить на shouldBe(Condition.visible)?
        element().parent()
                .parent()
                .$(".n2o-dropdown-menu")
                .shouldNotBe(Condition.hidden);
    }

    @Override
    public void shouldBeCollapsed() {
        element().parent()
                .parent()
                .$(".n2o-dropdown-menu")
                .shouldBe(Condition.hidden);
    }

    protected ElementsCollection menuItems() {
        return element().parent()
                .parent()
                .$$("div.dropdown-menu .btn.btn-secondary,.dropdown-item");
    }
}
