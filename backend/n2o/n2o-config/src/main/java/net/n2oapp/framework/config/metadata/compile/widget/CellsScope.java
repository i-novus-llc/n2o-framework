package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

import java.io.Serializable;
import java.util.List;

/**
 * Информация о ячейках таблицы
 */
@Getter
@Setter
public class CellsScope implements Serializable {
    List<N2oCell> cells;

    public CellsScope(List<N2oCell> cells) {
        this.cells = cells;
    }
}
