package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;

/**
 * Слияние двух источников данных
 */
public abstract class N2oDatasourceMerger<T extends N2oDatasource> implements BaseSourceMerger<T> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatasource.class;
    }

    @Override
    public T merge(T source, T override) {
        setIfNotNull(source::setSize, override::getSize);
        addIfNotNull(source, override, N2oDatasource::setDependencies, N2oDatasource::getDependencies);
        return source;
    }
}
