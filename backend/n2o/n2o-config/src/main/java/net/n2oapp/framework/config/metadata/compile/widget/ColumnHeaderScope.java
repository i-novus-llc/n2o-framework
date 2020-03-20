package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.io.Serializable;
import java.util.List;

/**
 * Информация для компиляции заголовков таблиц
 */
@Getter
@Setter
public class ColumnHeaderScope implements Serializable {
    private List<N2oCell> cells;
    private CompiledQuery query;

    public ColumnHeaderScope() {
    }

    public ColumnHeaderScope(List<N2oCell> cells, CompiledQuery query) {
        this.cells = cells;
        this.query = query;
    }
}
