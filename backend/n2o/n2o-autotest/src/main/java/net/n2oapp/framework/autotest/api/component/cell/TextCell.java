package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.impl.component.cell.N2oTextCell;

/**
 * Ячейка с текстом для автотестирования
 */
public interface TextCell extends Cell {
    void textShouldHave(String text);

    void subTextShouldHave(String... text);

    void shouldBeIconPosition(N2oTextCell.IconPosition position);
}
