package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRow;

/**
 * Абстрактная модель спискового виджета
 */
@Getter
@Setter
public abstract class N2oAbstractListWidget extends N2oWidget {
    @N2oAttribute("Настройки строк таблицы")
    private N2oRow rows;
    private N2oPagination pagination;
}
