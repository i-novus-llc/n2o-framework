package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.IconCell;

/**
 * Ячейка таблицы с иконкой для автотестирования
 */
public class N2oIconCell extends N2oCell implements IconCell{
    @Override
    public void iconShouldBe(String icon) {
        element().$(".n2o-icon").shouldBe(Condition.cssClass(icon));
    }

    @Override
    public void textShouldHave(String text) {
        element().$(".n2o-cell-text").shouldHave(Condition.text(text));
    }
}
