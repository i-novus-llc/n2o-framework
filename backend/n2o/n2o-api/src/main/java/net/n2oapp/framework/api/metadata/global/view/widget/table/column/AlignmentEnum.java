package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Выравнивание контента в ячейках таблицы
 */
@RequiredArgsConstructor
@Getter
public enum AlignmentEnum implements N2oEnum {
    LEFT("left"),
    RIGHT("right"),
    CENTER("center");

    private final String id;
}