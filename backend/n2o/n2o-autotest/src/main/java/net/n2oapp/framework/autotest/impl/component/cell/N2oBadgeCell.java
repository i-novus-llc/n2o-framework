package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;

public class N2oBadgeCell extends N2oCell implements BadgeCell {
    @Override
    public void colorShouldBe(Colors color) {
        element().$(".badge-" + color.name().toLowerCase()).shouldBe(Condition.exist);
    }

    @Override
    public void textShouldHave(String text) {
        element().shouldHave(Condition.text(text));
    }
}
