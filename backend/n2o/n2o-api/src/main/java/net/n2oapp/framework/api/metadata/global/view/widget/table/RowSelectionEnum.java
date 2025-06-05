package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Вариант выбора строки таблицы
 */
@RequiredArgsConstructor
@Getter
public enum RowSelectionEnum implements N2oEnum {
    NONE("none"),
    ACTIVE("active"),
    RADIO("radio"),
    CHECKBOX("checkbox");

    private final String id;
}