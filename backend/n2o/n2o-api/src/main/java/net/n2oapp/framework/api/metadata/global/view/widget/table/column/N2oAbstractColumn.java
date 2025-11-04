package net.n2oapp.framework.api.metadata.global.view.widget.table.column;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Исходная модель абстрактного столбца таблицы
 */
@Getter
@Setter
public abstract class N2oAbstractColumn implements IdAware, Source {
    private String id;
    private String src;
    private ColumnFixedPositionEnum fixed;
}
