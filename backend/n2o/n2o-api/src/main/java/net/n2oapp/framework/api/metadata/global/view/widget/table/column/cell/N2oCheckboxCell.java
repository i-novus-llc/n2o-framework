package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Ячейка таблицы с checkbox
 */
@Getter
@Setter
@VisualComponent
public class N2oCheckboxCell extends N2oActionCell {
    @VisualAttribute
    private String enabled;
}
