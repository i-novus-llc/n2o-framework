package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Простая ячейка с текстом
 */
@Getter
@Setter
@VisualComponent
public class N2oTextCell extends N2oAbstractCell {
    private N2oSwitch classSwitch;
    @VisualAttribute
    private String format;
    @VisualAttribute
    private String subTextFieldKey;
    @VisualAttribute
    private String subTextFormat;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private Position iconPosition;
}
