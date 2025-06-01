package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oBrowserStorageDatasource;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        compileDatasource(source, compiled, p);
        compiled.setProvider(initProvider(source, p));
        compiled.setSubmit(initSubmit(source, p));
        compiled.setFetchOnInit(castDefault(source.getFetchOnInit(), () -> p.resolve(property("n2o.api.datasource.browser.fetch_on_init"), Boolean.class)));
        return compiled;
    }

    private BrowserStorageDatasource.Provider initProvider(N2oBrowserStorageDatasource source, CompileProcessor p) {
        BrowserStorageDatasource.Provider provider = new BrowserStorageDatasource.Provider();
        provider.setKey(castDefault(source.getKey(), source.getId()));
        provider.setStorage(castDefault(source.getStorageType(),
                () -> p.resolve(property("n2o.api.datasource.browser.storage_type"), BrowserStorageTypeEnum.class)));
        return provider;
    }

    private BrowserStorageDatasource.Submit initSubmit(N2oBrowserStorageDatasource source, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        BrowserStorageDatasource.Submit submit = new BrowserStorageDatasource.Submit();
        submit.setKey(castDefault(source.getSubmit().getKey(), source.getKey(), source.getId()));
        submit.setAuto(castDefault(source.getSubmit().getAuto(),
                () -> p.resolve(property("n2o.api.datasource.browser.submit.auto"), Boolean.class)));
        submit.setModel(castDefault(source.getSubmit().getModel(), ReduxModelEnum.RESOLVE));
        submit.setStorage(castDefault(source.getSubmit().getStorageType(), source.getStorageType(), BrowserStorageTypeEnum.SESSION_STORAGE));
        return submit;
    }
}
