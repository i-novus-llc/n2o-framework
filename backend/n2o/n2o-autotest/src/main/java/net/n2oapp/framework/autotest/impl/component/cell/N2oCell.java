package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Ячейка списковых виджетов (table, list) для автотестирования
 */
public class N2oCell extends N2oComponent implements Cell {

    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }
}
