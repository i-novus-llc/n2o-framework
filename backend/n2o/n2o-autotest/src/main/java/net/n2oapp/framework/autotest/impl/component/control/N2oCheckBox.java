package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;

/**
 * Компонент Checkbox для автотестирования
 */
public class N2oCheckBox extends N2oControl implements Checkbox {

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

    @Override
    public void shouldBeUnchecked() {
        element().shouldNotBe(Condition.checked);
    }
}
