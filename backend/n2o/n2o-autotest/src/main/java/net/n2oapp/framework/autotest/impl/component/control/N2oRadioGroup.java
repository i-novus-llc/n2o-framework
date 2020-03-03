package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;

/**
 * Компонент радиокнопок (radio-group) для автотестирования
 */
public class N2oRadioGroup extends N2oControl implements RadioGroup {

    @Override
    public void shouldBeChecked(String label) {
        element().$(".checked label").shouldHave(Condition.text(label));
    }

    @Override
    public void check(String label) {
        element().$$("label").findBy(Condition.text(label)).click();
    }
}
