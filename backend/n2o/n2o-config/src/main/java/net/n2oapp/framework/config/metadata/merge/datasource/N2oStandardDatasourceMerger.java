package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух стандартных источников данных
 */
@Component
public class N2oStandardDatasourceMerger implements BaseSourceMerger<N2oStandardDatasource> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardDatasource.class;
    }

    @Override
    public N2oStandardDatasource merge(N2oStandardDatasource ref, N2oStandardDatasource source) {
        setIfNotNull(source::setSize, source::getSize, ref::getSize);
        setIfNotNull(source::setDefaultValuesMode, source::getDefaultValuesMode, ref::getDefaultValuesMode);
        setIfNotNull(source::setQueryId, source::getQueryId, ref::getQueryId);
        setIfNotNull(source::setObjectId, source::getObjectId, ref::getObjectId);
        setIfNotNull(source::setSubmit, source::getSubmit, ref::getSubmit);
        addIfNotNull(ref, source, N2oStandardDatasource::setDependencies, N2oStandardDatasource::getDependencies);
        addIfNotNull(ref, source, N2oStandardDatasource::setFilters, N2oStandardDatasource::getFilters);
        return source;
    }
}

