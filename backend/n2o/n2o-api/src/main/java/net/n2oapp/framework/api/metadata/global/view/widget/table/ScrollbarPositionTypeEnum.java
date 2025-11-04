package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Расположение горизонтального скрол-бара в таблице
 */

@RequiredArgsConstructor
@Getter
public enum ScrollbarPositionTypeEnum implements N2oEnum {
    TOP("top"),
    BOTTOM("bottom");

    private final String id;
}
