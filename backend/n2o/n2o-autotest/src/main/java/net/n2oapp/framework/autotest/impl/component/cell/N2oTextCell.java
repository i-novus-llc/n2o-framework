package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;

/**
 * Текстовая ячейка таблицы для автотестирования
 */
public class N2oTextCell extends N2oCell implements TextCell {

    @Override
    public void textShouldHave(String text) {
        element().shouldHave(Condition.text(text));
    }
}
