package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractSwitch;

/**
 * Исходная модель переключателя ячеек
 */
@Getter
@Setter
public class N2oSwitchCell extends N2oAbstractSwitch<N2oAbstractCell> implements N2oCell {
    private String id;
    private String src;
}
