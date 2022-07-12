package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oBrowserStorageDatasource;
import org.springframework.stereotype.Component;

/**
 * Слияние двух источников, хранящих данные в браузере
 */
@Component
public class N2oBrowserStorageDatasourceMerger extends N2oDatasourceMerger<N2oBrowserStorageDatasource> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBrowserStorageDatasource.class;
    }

    @Override
    public N2oBrowserStorageDatasource merge(N2oBrowserStorageDatasource source, N2oBrowserStorageDatasource override) {
        super.merge(source, override);
        setIfNotNull(source::setKey, override::getKey);
        setIfNotNull(source::setStorageType, override::getStorageType);
        setIfNotNull(source::setSubmit, override::getSubmit);
        return source;
    }
}
