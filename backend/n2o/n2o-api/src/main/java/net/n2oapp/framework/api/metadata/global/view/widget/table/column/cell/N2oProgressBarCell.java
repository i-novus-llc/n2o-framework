package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка с индикатором прогресса
 */
@Getter
@Setter
public class N2oProgressBarCell extends N2oAbstractCell {
    private Size size;
    private Boolean striped;
    private Boolean active;
    private String color;

    public enum Size {
        small, normal, large
    }
}
