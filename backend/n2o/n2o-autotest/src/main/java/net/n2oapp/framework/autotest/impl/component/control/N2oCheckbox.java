package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;

/**
 * Компонент чекбокса для автотестирования
 */
public class N2oCheckbox extends N2oControl implements Checkbox {

    @Override
    public void shouldBeEmpty() {
        element().shouldNotBe(Condition.checked);
    }

    @Override
    public boolean isChecked() {
        return element().isSelected();
    }

    @Override
    public void setChecked(boolean val) {
        //FIXME element().shouldBe(Condition.exist).setSelected(value);
        if (val != isChecked())
            element().shouldBe(Condition.exist).parent().$("label").click();
    }

    @Override
    public void shouldBeChecked() {
        element().shouldBe(Condition.checked);
    }
}
