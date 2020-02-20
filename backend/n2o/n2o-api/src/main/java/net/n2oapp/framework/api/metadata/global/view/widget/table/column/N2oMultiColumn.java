package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.Setter;

/**
 * Многоуровневый столбец таблицы
 */
@Getter
@Setter
public class N2oMultiColumn extends AbstractColumn {
    private AbstractColumn[] children;

    @Override
    public boolean isDynamic() {
        return false;
    }
}
