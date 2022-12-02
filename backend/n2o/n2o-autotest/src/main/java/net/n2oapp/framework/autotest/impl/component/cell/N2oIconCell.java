package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.cell.IconCell;

/**
 * Ячейка таблицы с иконкой для автотестирования
 */
public class N2oIconCell extends N2oCell implements IconCell {
    @Override
    public void iconShouldBe(String icon) {
        element().$(".n2o-icon").shouldBe(Condition.cssClass(icon));
    }

    @Override
    public void textShouldHave(String text) {
        element().$(".n2o-cell-text").shouldHave(Condition.text(text));
    }

    @Override
    public void hover() {
        element().$(".n2o-icon").hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().$(".tooltip-container");
        return N2oSelenide.component(element, Tooltip.class);
    }
}
