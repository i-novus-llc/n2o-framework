package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oBrowserStorageDatasource;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция источника, хранящего данные в браузере
 */
@Component
public class BrowserStorageDatasourceCompiler extends BaseDatasourceCompiler<N2oBrowserStorageDatasource, BrowserStorageDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBrowserStorageDatasource.class;
    }

    @Override
    public BrowserStorageDatasource compile(N2oBrowserStorageDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        BrowserStorageDatasource compiled = new BrowserStorageDatasource();
        initDatasource(compiled, source, context, p);
        compiled.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.widget.table.size"), Integer.class)));
        compiled.setProvider(initProvider(source, p));
        compiled.setSubmit(initSubmit(source, p));
        compiled.setDependencies(initDependencies(source, p));
        compiled.setValidations(initValidations(source, p));
        return compiled;
    }

    private BrowserStorageDatasource.Provider initProvider(N2oBrowserStorageDatasource source, CompileProcessor p) {
        BrowserStorageDatasource.Provider provider = new BrowserStorageDatasource.Provider();
        provider.setKey(p.cast(source.getKey(), source.getId()));
        provider.setStorage(p.cast(source.getStorageType(), p.resolve(Placeholders.property("n2o.api.datasource.browser.storage_type"), BrowserStorageType.class)));
        return provider;
    }

    private BrowserStorageDatasource.Submit initSubmit(N2oBrowserStorageDatasource source, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        BrowserStorageDatasource.Submit submit = new BrowserStorageDatasource.Submit();
        submit.setKey(p.cast(source.getSubmit().getKey(), source.getKey(), source.getId()));
        submit.setAuto(p.cast(source.getSubmit().getAuto(), p.resolve(property("n2o.api.datasource.browser.submit.auto"), Boolean.class)));
        submit.setModel(p.cast(source.getSubmit().getModel(), ReduxModel.resolve));
        submit.setStorage(p.cast(source.getSubmit().getStorageType(), source.getStorageType(), BrowserStorageType.sessionStorage));
        return submit;
    }
}
