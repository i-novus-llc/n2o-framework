package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.N2oQueryDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух источников данных
 */
@Component
public class N2oQueryDatasourceMerger implements BaseSourceMerger<N2oQueryDatasource> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQueryDatasource.class;
    }

    @Override
    public N2oQueryDatasource merge(N2oQueryDatasource source, N2oQueryDatasource override) {
        setIfNotNull(source::setDefaultValuesMode, override::getDefaultValuesMode);
        setIfNotNull(source::setQueryId, override::getQueryId);
        setIfNotNull(source::setSize, override::getSize);
        setIfNotNull(source::setObjectId, override::getObjectId);
        setIfNotNull(source::setSubmit, override::getSubmit);
        addIfNotNull(source, override, N2oQueryDatasource::setFilters, N2oQueryDatasource::getFilters);
        addIfNotNull(source, override, N2oQueryDatasource::setDependencies, N2oQueryDatasource::getDependencies);
        return source;
    }
}
