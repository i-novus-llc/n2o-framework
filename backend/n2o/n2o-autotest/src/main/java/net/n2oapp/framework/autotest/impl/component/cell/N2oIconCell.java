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
    public void shouldHaveIcon(String icon) {
        icon().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void shouldHaveText(String text) {
        element().$(".n2o-cell-text").shouldHave(Condition.exactText(text));
    }

    @Override
    public void hover() {
        icon().hover();
    }

    @Override
    public Tooltip tooltip() {
        SelenideElement element = element().$(".tooltip-container");
        return N2oSelenide.component(element, Tooltip.class);
    }

    protected SelenideElement icon() {
        return element().$(".n2o-icon");
    }
}
