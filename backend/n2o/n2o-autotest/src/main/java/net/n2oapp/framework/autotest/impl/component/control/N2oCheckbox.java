package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;

import java.time.Duration;

/**
 * Компонент чекбокса для автотестирования
 */
public class N2oCheckbox extends N2oControl implements Checkbox {

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        //ToDo нужен ли shouldBeChecked, если value у checkbox это и есть true или false
        throw new UnsupportedOperationException();
    }

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
        //FIXME element().shouldBe(Condition.exist).setSelected(value); , but it not be possible now, because of input is invisible
        if (val != isChecked())
            element().shouldBe(Condition.exist).parent().$("label").click();
    }

    @Override
    public void shouldBeChecked() {
        element().shouldBe(Condition.checked);
    }

    @Override
    public void shouldNotBeChecked() {
        element().shouldNotBe(Condition.checked);
    }
}
