package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;


/**
 * Исходная модель многоуровневого столбца таблицы
 */

@Getter
@Setter
public class N2oMultiColumn extends N2oBaseColumn {
    private N2oAbstractColumn[] children;
}
