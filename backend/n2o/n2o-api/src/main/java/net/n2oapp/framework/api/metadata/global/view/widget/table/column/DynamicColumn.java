package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oDynamicSwitch;

/**
 * @author V. Alexeev.
 */
public class DynamicColumn extends AbstractColumn {

    private N2oDynamicSwitch<N2oCell> n2oSwitch;

    public N2oDynamicSwitch<N2oCell> getN2oSwitch() {
        return n2oSwitch;
    }

    public void setN2oSwitch(N2oDynamicSwitch<N2oCell> n2oSwitch) {
        this.n2oSwitch = n2oSwitch;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
