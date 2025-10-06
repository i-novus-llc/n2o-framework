package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Исходная модель элемента меню {@code <group>}
 */
@Getter
@Setter
public class N2oGroupMenuItem extends N2oAbstractMenuItem {
    private Boolean collapsible;
    private GroupStateTypeEnum defaultState;
    private N2oAbstractMenuItem[] menuItems;

    @RequiredArgsConstructor
    @Getter
    public enum GroupStateTypeEnum implements N2oEnum {
        EXPANDED("expanded"),
        COLLAPSED("collapsed");

        private final String id;
    }
}