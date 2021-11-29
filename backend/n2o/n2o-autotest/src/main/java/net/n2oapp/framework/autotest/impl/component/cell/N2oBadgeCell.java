package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;

/**
 * Ячейка таблицы с текстом для автотестирования
 */
public class N2oBadgeCell extends N2oCell implements BadgeCell {
    @Override
    public void colorShouldBe(Colors color) {
        element().$(".badge").shouldBe(Condition.cssClass(color.name("badge-")));
    }

    @Override
    public void textShouldHave(String text) {
        element().shouldHave(Condition.text(text));
    }
}
