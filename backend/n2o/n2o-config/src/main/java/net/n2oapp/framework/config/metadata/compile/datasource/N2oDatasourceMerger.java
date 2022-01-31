package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух источников данных
 */
@Component
public class N2oDatasourceMerger implements BaseSourceMerger<N2oDatasource> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatasource.class;
    }

    @Override
    public N2oDatasource merge(N2oDatasource source, N2oDatasource override) {
        setIfNotNull(source::setDefaultValuesMode, override::getDefaultValuesMode);
        setIfNotNull(source::setQueryId, override::getQueryId);
        setIfNotNull(source::setSize, override::getSize);
        setIfNotNull(source::setObjectId, override::getObjectId);
        setIfNotNull(source::setSubmit, override::getSubmit);
        addIfNotNull(source, override, N2oDatasource::setFilters, N2oDatasource::getFilters);
        addIfNotNull(source, override, N2oDatasource::setDependencies, N2oDatasource::getDependencies);
        return source;
    }
}
