package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.RadioCell;

/**
 * Ячейка таблицы с radio для автотестирования
 */

public class N2oRadioCell extends N2oCell implements RadioCell {

    @Override
    public void click() {
        radioElement().click();
    }

    @Override
    public void shouldBeChecked() {
        radioElement().shouldHave(Condition.cssClass("checked"));
    }

    @Override
    public void shouldBeUnchecked() {
        radioElement().shouldNotHave(Condition.cssClass("checked"));
    }

    private SelenideElement radioElement() {
        return element().$(".n2o-radio-input");
    }
}
