package net.n2oapp.framework.config.metadata.merge.widget;

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
        setIfNotNull(source::setPagination, override::getPagination);
        setIfNotNull(source::setRows, override::getRows);
        setIfNotNull(source::setAutoSelect, override::getAutoSelect);
        setIfNotNull(source::setSelection, override::getSelection);
        setIfNotNull(source::setWidth, override::getWidth);
        setIfNotNull(source::setHeight, override::getHeight);
        setIfNotNull(source::setTextWrap, override::getTextWrap);
        if (override.getFilters() != null) {
            if (source.getFilters() == null)
                source.setFilters(new N2oTable.N2oTableFilters());
            setIfNotNull(source.getFilters()::setPlace, override.getFilters()::getPlace);
            setIfNotNull(source.getFilters()::setDatasourceId, override.getFilters()::getDatasourceId);
            setIfNotNull(source.getFilters()::setDatasource, override.getFilters()::getDatasource);
            setIfNotNull(source.getFilters()::setFetchOnChange, override.getFilters()::getFetchOnChange);
            setIfNotNull(source.getFilters()::setFetchOnClear, override.getFilters()::getFetchOnClear);
            addIfNotNull(source.getFilters(), override.getFilters(), N2oTable.N2oTableFilters::setItems, N2oTable.N2oTableFilters::getItems);
        }
        setIfNotNull(source::setChildren, override::getChildren);
        addIfNotNull(source, override, N2oTable::setColumns, N2oTable::getColumns);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
