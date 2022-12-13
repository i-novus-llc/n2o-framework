package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.EditType;

/**
 * Ячейка позволяющая изменять содержимое таблицы
 */
@Getter
@Setter
@VisualComponent
public class N2oEditCell extends N2oActionCell {
    @VisualAttribute
    private N2oField n2oField;
    @VisualAttribute
    private String format;
    @VisualAttribute
    private String editFieldId;
    @VisualAttribute
    private EditType editType;
    @VisualAttribute
    private String enabled;
}
