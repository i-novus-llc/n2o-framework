package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

/**
 * Столбец таблицы
 */
@Getter
@Setter
@VisualComponent
public class N2oSimpleColumn extends AbstractColumn {
    @VisualAttribute
    private N2oCell cell;
}
