package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;

public class N2oCheckboxCell extends N2oCell implements CheckboxCell {
    @Override
    public boolean isChecked() {
        return element().$(".n2o-checkbox .n2o-input").isSelected();
    }

    @Override
    public void setChecked(boolean val) {
        //FIXME element().shouldBe(Condition.exist).setSelected(value);
        if (val != isChecked())
            element().shouldBe(Condition.exist).$(".n2o-checkbox").click(-5,0);
    }

    @Override
    public void shouldBeChecked() {
        element().$(".n2o-checkbox .n2o-input").shouldBe(Condition.checked);
    }

    @Override
    public void shouldBeUnchecked() {
        element().$(".n2o-checkbox .n2o-input").shouldNotBe(Condition.checked);
    }
}
