package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;

import java.util.List;

/**
 * Информация о ячейках таблицы
 */
@Getter
@Setter
public class CellsScope {
    private List<Cell> cells;

    public CellsScope() {
    }

    public CellsScope(List<Cell> cells) {
        this.cells = cells;
    }
}
