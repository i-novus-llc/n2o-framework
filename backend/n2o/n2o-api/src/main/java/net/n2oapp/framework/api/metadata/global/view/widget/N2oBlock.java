package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;

/**
 * Исходная модель блока плитки и карточки
 */
@Getter
@Setter
public class N2oBlock extends N2oBaseColumn {
    private N2oCell component;
}
