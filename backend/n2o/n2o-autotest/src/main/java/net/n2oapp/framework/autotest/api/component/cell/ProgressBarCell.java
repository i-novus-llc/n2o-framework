package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.autotest.Colors;

/**
 * Ячейка таблицы с ProgressBar для автотестирования
 */
public interface ProgressBarCell extends Cell {

    void colorShouldBe(Colors color);

    void valueShouldBe(String value);

    void sizeShouldBe(Size size);

    void shouldBeAnimated();

    void shouldBeStriped();

    /**
     * {@link N2oProgressBarCell.Size}
     */
    enum Size {
        small, normal, large
    }
}
