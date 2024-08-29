package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.IconCell;

import java.time.Duration;

/**
 * Ячейка таблицы с иконкой для автотестирования
 */
public class N2oIconCell extends N2oCell implements IconCell {
    @Override
    public void shouldHaveIcon(String icon) {
        icon().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.exactText(text), element().$(".n2o-cell-text"), duration);
    }

    @Override
    public void hover() {
        icon().hover();
    }

    protected SelenideElement icon() {
        return element().$(".n2o-icon");
    }
}
