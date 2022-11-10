package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными источника, хранящего данные в браузере
 */
@Component
public class BrowserStorageDatasourceBinder implements BaseMetadataBinder<BrowserStorageDatasource> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return BrowserStorageDatasource.class;
    }

    @Override
    public BrowserStorageDatasource bind(BrowserStorageDatasource compiled, BindProcessor p) {
        if (compiled.getProvider() != null)
            compiled.getProvider().setKey(p.resolveText(compiled.getProvider().getKey()));
        if (compiled.getSubmit() != null)
            compiled.getSubmit().setKey(p.resolveText(compiled.getSubmit().getKey()));
        return compiled;
    }
}
