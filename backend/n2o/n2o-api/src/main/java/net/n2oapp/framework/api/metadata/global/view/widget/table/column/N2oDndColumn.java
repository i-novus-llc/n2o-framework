package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель drag-n-drop столбца таблицы
 */

@Getter
@Setter
public class N2oDndColumn extends N2oAbstractColumn {
    private MoveModeEnum moveMode;
    private N2oAbstractColumn[] children;
}
