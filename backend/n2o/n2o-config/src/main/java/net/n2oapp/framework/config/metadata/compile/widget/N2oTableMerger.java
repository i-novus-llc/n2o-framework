package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import org.springframework.stereotype.Component;

/**
 * Слияние двух виджетов Таблица
 */
@Component
public class N2oTableMerger extends N2oWidgetMerger<N2oTable> {
    @Override
    public N2oTable merge(N2oTable source, N2oTable override) {
        setIfNotNull(source::setSelection, override::getSelection);
        setIfNotNull(source::setScrollX, override::getScrollX);
        setIfNotNull(source::setScrollY, override::getScrollY);
        setIfNotNull(source::setTableSize, override::getTableSize);
        setIfNotNull(source::setChildren, override::getChildren);
        setIfNotNull(source::setPagination, override::getPagination);
        setIfNotNull(source::setRows, override::getRows);
        addIfNotNull(source, override, N2oTable::setColumns, N2oTable::getColumns);
        addIfNotNull(source, override, N2oTable::setFilters, N2oTable::getFilters);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
