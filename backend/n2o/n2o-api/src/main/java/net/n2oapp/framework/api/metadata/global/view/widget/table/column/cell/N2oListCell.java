package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Ячейка со списком
 */
@Getter
@Setter
@VisualComponent
public class N2oListCell extends N2oAbstractCell {
    @VisualAttribute
    private String labelFieldId;
    @VisualAttribute
    private String color;
    @VisualAttribute
    private Integer size;
    private N2oSwitch n2oSwitch;
}
