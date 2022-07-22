package net.n2oapp.framework.api.metadata.meta.widget.table;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель таблицы
 */
@Getter
@Setter
public class Table<C extends TableWidgetComponent> extends AbstractTable<C> {

    public Table(C component) {
        super(component);
    }
}

