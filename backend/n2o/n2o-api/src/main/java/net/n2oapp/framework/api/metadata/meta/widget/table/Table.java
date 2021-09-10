package net.n2oapp.framework.api.metadata.meta.widget.table;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель таблицы
 */
@Getter
@Setter
public class Table extends AbstractTable<TableWidgetComponent> {

    public Table() {
        super(new TableWidgetComponent());
    }
}

