package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

/**
 * Панель действий для автотестирования
 */
public class N2oToolbar extends N2oComponentsCollection implements Toolbar {
    @Override
    public StandardButton button(String label) {
        return button(label, StandardButton.class);
    }

    @Override
    public StandardButton button(WebElementCondition findBy) {
        return button(findBy, StandardButton.class);
    }

    @Override
    public DropdownButton dropdown() {
        return button(Condition.cssClass("n2o-dropdown-control"), DropdownButton.class);
    }

    @Override
    public DropdownButton dropdown(WebElementCondition findBy) {
        return button(findBy, DropdownButton.class);
    }

    @Override
    public <T extends Button> T button(int index, Class<T> componentClass) {
        return N2oSelenide.component(elements().get(index), componentClass);
    }

    @Override
    public <T extends Button> T button(String label, Class<T> componentClass) {
        return N2oSelenide.component(elements().findBy(Condition.text(label)), componentClass);
    }

    @Override
    public <T extends Button> T button(WebElementCondition findBy, Class<T> componentClass) {
        return N2oSelenide.component(elements().findBy(findBy), componentClass);
    }
}
