package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Простая ячейка с текстом
 */
@Getter
@Setter
public class N2oTextCell extends N2oAbstractCell {
    private N2oSwitch classSwitch;
    private String format;
    private String subTextFieldKey;
    private String subTextFormat;
}
