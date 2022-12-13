package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Ячейка с индикатором прогресса
 */
@Getter
@Setter
@VisualComponent
public class N2oProgressBarCell extends N2oAbstractCell {
    @VisualAttribute
    private Size size;
    @VisualAttribute
    private Boolean striped;
    @VisualAttribute
    private Boolean active;
    @VisualAttribute
    private String color;

    public enum Size {
        small, normal, large
    }
}
