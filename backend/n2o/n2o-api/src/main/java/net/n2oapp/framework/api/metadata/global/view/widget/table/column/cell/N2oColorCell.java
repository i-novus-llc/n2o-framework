package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Ячейки с определенным цветом текста
 */
@Deprecated
public class N2oColorCell extends N2oAbstractCell {
    private N2oSwitch styleSwitch;

    public N2oColorCell(N2oSwitch styleSwitch) {
        this.styleSwitch = styleSwitch;
    }

    public N2oSwitch getStyleSwitch() {
        return styleSwitch;
    }

    public void setStyleSwitch(N2oSwitch styleSwitch) {
        this.styleSwitch = styleSwitch;
    }

}
