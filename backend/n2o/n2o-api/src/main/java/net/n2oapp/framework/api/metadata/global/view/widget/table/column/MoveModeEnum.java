package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Режим перемещения колонки
 */
@RequiredArgsConstructor
@Getter
public enum MoveModeEnum implements N2oEnum {
    TABLE("table"),
    RIGHT("settings"),
    ALL("all");

    private final String id;
}