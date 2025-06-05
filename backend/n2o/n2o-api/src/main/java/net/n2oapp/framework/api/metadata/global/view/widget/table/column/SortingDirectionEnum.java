package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Направление сортировки по умолчанию
 */
@RequiredArgsConstructor
@Getter
public enum SortingDirectionEnum implements N2oEnum {
    ASC("asc"),
    DESC("desc");

    private final String id;
}