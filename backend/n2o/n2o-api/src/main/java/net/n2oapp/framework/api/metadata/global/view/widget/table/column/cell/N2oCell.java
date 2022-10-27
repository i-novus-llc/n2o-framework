package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;


import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;


/**
 * Интерфейс ячеек списковых виджетов
 */
public interface N2oCell extends Source, SrcAware, IdAware, NamespaceUriAware {

}
