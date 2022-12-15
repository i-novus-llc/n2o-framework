package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

/**
 * Столбец таблицы
 */
@Getter
@Setter
@N2oComponent
public class N2oSimpleColumn extends AbstractColumn {
    @N2oAttribute
    private N2oCell cell;
}
