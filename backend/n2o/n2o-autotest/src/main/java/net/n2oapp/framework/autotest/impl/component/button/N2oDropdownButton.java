package net.n2oapp.framework.autotest.impl.component.button;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

import java.time.Duration;

public class N2oDropdownButton extends N2oButton implements DropdownButton {

    private final String DROPDOWN = ".n2o-dropdown-menu,.dropdown-menu";

    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldHaveLabel(String label, Duration... duration) {
        should(Condition.exactText(label), duration);
    }

    @Override
    public void shouldHaveDescription(String description, Duration... duration) {
        should(Condition.text(description), duration);
    }

    @Override
    public void shouldHaveItems(int count) {
        menuItems().shouldHave(CollectionCondition.size(count));
    }

    @Override
    public StandardButton menuItem(String label) {
        return menuItem(Condition.text(label));
    }

    @Override
    public StandardButton menuItem(Condition by) {
        return N2oSelenide.component(menuItems().findBy(by), N2oStandardButton.class);
    }

    @Override
    public void shouldBeVisible() {
        element().parent().shouldBe(Condition.visible);
    }

    @Override
    public void shouldBeHidden() {
        element().parent().shouldBe(Condition.hidden);
    }

    @Deprecated
    public void shouldNotBeVisible() {
        shouldBeHidden();
    }

    @Override
    public void shouldBeExpanded() {
        dropdownMenu().shouldBe(Condition.visible);
    }

    @Override
    public void shouldBeCollapsed() {
        dropdownMenu().shouldBe(Condition.hidden);
    }

    @Override
    public void shouldHaveIcon(String iconName) {
        element().$("i").shouldHave(Condition.cssClass(iconName));
    }

    private SelenideElement dropdownMenu() {
        if (element().parent().$(DROPDOWN).exists())
            return element().parent().$(DROPDOWN);
        return element().parent().parent().$(DROPDOWN);
    }

    protected ElementsCollection menuItems() {
        return dropdownMenu()
                .$$("div.dropdown-menu .btn.btn-secondary,.dropdown-item");
    }
}
