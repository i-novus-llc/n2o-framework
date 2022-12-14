package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Ячейка со списком
 */
@Getter
@Setter
public class N2oListCell extends N2oAbstractCell {
    private String labelFieldId;
    private String color;
    private Integer size;
    private N2oSwitch n2oSwitch;
}
