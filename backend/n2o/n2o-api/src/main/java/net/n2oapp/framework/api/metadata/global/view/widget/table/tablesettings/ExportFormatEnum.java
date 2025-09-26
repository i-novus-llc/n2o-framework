package net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Формат экспорта данных таблицы
 */
@RequiredArgsConstructor
@Getter
public enum ExportFormatEnum implements N2oEnum {
    CSV("csv"),
    XLSX("xlsx");

    private final String id;
}