package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;

/**
 * Исходная модель элемента меню {@code <dropdown-menu>}
 */
@Getter
@Setter
public class N2oDropdownMenuItem extends N2oAbstractMenuItem {
    private TriggerEnum trigger;
    private PositionTypeEnum position;
    private N2oAbstractMenuItem[] menuItems;

    @RequiredArgsConstructor
    @Getter
    public enum PositionTypeEnum implements N2oEnum {
        TOP("top"),
        LEFT("left"),
        RIGHT("right"),
        BOTTOM("bottom"),
        AUTO("auto");

        private final String id;
    }
}