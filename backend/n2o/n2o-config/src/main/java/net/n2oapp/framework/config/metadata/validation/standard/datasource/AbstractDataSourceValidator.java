package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;

public abstract class AbstractDataSourceValidator<S extends N2oAbstractDatasource> implements SourceValidator<S>, SourceClassAware {

    @Override
    public void validate(S source, SourceProcessor p) {

    }

}
