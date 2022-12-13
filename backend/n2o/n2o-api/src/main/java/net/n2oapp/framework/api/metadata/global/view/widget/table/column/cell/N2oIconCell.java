package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Ячейка с иконкой
 */
@Getter
@Setter
@VisualComponent
public class N2oIconCell extends N2oAbstractCell {
    private N2oSwitch iconSwitch;
    @VisualAttribute
    private IconType iconType;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private String text;
    @VisualAttribute
    private Position position;
}
