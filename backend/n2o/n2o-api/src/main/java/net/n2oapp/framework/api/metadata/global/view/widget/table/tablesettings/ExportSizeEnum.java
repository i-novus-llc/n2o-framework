package net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Количество записей таблицы для экспорта
 */
@RequiredArgsConstructor
@Getter
public enum ExportSizeEnum implements N2oEnum {
    ALL("all"),
    PAGE("page");

    private final String id;
}