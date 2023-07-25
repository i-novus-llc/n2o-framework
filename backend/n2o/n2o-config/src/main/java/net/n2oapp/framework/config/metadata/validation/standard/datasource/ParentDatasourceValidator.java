package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;

public class ParentDatasourceValidator extends AbstractDataSourceValidator<N2oParentDatasource>{

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oParentDatasource.class;
    }
}
