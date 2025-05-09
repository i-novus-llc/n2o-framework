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
    public N2oTable merge(N2oTable ref, N2oTable source) {
        setIfNotNull(source::setPagination, source::getPagination, ref::getPagination);
        setIfNotNull(source::setRows, source::getRows, ref::getRows);
        setIfNotNull(source::setAutoSelect, source::getAutoSelect, ref::getAutoSelect);
        setIfNotNull(source::setSelection, source::getSelection, ref::getSelection);
        setIfNotNull(source::setWidth, source::getWidth, ref::getWidth);
        setIfNotNull(source::setHeight, source::getHeight, ref::getHeight);
        setIfNotNull(source::setTextWrap, source::getTextWrap, ref::getTextWrap);
        if (source.getFilters() != null) {
            if (ref.getFilters() == null)
                ref.setFilters(new N2oTable.N2oTableFilters());
            setIfNotNull(source.getFilters()::setPlace, source.getFilters()::getPlace, ref.getFilters()::getPlace);
            setIfNotNull(source.getFilters()::setDatasourceId, source.getFilters()::getDatasourceId, ref.getFilters()::getDatasourceId);
            setIfNotNull(source.getFilters()::setDatasource, source.getFilters()::getDatasource, ref.getFilters()::getDatasource);
            setIfNotNull(source.getFilters()::setFetchOnChange, source.getFilters()::getFetchOnChange, ref.getFilters()::getFetchOnChange);
            setIfNotNull(source.getFilters()::setFetchOnClear, source.getFilters()::getFetchOnClear, ref.getFilters()::getFetchOnClear);
            addIfNotNull(ref.getFilters(), source.getFilters(), N2oTable.N2oTableFilters::setItems, N2oTable.N2oTableFilters::getItems);
        }
        setIfNotNull(source::setChildren, source::getChildren, ref::getChildren);
        addIfNotNull(ref, source, N2oTable::setColumns, N2oTable::getColumns);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
