package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

/**
 * Столбец таблицы
 */
@Getter
@Setter
public class N2oSimpleColumn extends AbstractColumn {
    private N2oCell cell;
}
