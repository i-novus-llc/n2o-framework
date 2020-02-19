package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;

/**
 * Фильтруемый столбец таблицы
 */
@Getter
@Setter
public class N2oFilterColumn extends N2oSimpleColumn {
    private SourceComponent filter;
}
