package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oBrowserStorageDatasource;

public class BrowserStorageDataSourceValidator extends DatasourceValidator<N2oBrowserStorageDatasource>{

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBrowserStorageDatasource.class;
    }
}
