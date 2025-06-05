package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Позиция фиксации столбца
 */
@RequiredArgsConstructor
@Getter
public enum ColumnFixedPositionEnum implements N2oEnum {
    LEFT("left"),
    RIGHT("right");

    private final String id;
}