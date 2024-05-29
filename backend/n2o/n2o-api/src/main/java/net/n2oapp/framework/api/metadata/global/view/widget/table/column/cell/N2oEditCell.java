package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oField;

/**
 * Ячейка позволяющая изменять содержимое таблицы
 */
@Getter
@Setter
public class N2oEditCell extends N2oActionCell {
    private N2oField n2oField;
    private String format;
    private String enabled;
}
