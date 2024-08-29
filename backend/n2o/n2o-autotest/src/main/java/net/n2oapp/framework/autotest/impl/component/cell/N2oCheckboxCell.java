package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;

/**
 * Ячейка таблицы с Checkbox для автотестирования
 */
public class N2oCheckboxCell extends N2oCell implements CheckboxCell {

    @Override
    public SelenideElement element() {
        return super.element().$(".n2o-checkbox .n2o-input");
    }

    @Override
    public boolean isChecked() {
        return element().isSelected();
    }

    @Override
    public void setChecked(boolean val) {
        //FIXME element().shouldBe(Condition.exist).setSelected(value); UPD: не может быть пофикшено, тк invisible
        if (val != isChecked())
            element().shouldBe(Condition.exist).parent().$(".custom-control-label").click();
    }

    @Override
    public void shouldBeChecked() {
        element().shouldBe(Condition.checked);
    }

    @Override
    public void shouldBeUnchecked() {
        element().shouldNotBe(Condition.checked);
    }

    @Override
    public void shouldBeEnabled() {
        element().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        element().shouldBe(Condition.disabled);
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().parent().$(".tooltip-inner");
        return N2oSelenide.component(element, Tooltip.class);
    }
}
