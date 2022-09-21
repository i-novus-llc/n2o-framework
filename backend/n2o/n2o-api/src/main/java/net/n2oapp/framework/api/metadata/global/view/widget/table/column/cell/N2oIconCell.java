package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Ячейка с иконкой
 */
@Getter
@Setter
public class N2oIconCell extends N2oAbstractCell {
    private N2oSwitch iconSwitch;
    private IconType iconType;
    private String icon;
    private String text;
    private Position position;
}
