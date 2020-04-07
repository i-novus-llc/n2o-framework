package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;

/**
 * Панель действий для автотестирования
 */
public interface Toolbar extends ComponentsCollection {

    StandardButton button(String label);

    StandardButton button(Condition findBy);

    DropdownButton dropdown(String label);

    DropdownButton dropdown(Condition findBy);

    <T extends Button> T button(String label, Class<T> componentClass);

    <T extends Button> T button(Condition findBy, Class<T> componentClass);

    <T extends Button> T button(int index, Class<T> componentClass);
}
