package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.util.BindUtil;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными источника данных
 */
@Component
public class StandardDatasourceBinder implements BaseMetadataBinder<StandardDatasource> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return StandardDatasource.class;
    }

    @Override
    public StandardDatasource bind(StandardDatasource datasource, BindProcessor p) {
        if (datasource.getProvider() != null) {
            BindUtil.bindDataProvider(datasource.getProvider(), p);
        }
        if (datasource.getSubmit() != null) {
            BindUtil.bindDataProvider(datasource.getSubmit(), p);
        }
        return datasource;
    }
}
