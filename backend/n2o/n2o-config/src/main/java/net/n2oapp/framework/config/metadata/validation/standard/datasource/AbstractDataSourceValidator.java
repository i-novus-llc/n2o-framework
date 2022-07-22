package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;

public abstract class AbstractDataSourceValidator<T extends N2oAbstractDatasource> implements SourceValidator<T>, SourceClassAware {

}
