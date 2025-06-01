package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageTypeEnum;
import net.n2oapp.framework.api.metadata.datasource.CachedDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oCachedDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.datasource.DatasourceCompileStaticProcessor.*;

/**
 * Компиляция кэширующего источника данных
 */
@Component
public class CachedDatasourceCompiler extends BaseDatasourceCompiler<N2oCachedDatasource, CachedDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCachedDatasource.class;
    }

    @Override
    public CachedDatasource compile(N2oCachedDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        CachedDatasource compiled = new CachedDatasource();
        compileDatasource(source, compiled, p);
        CompiledQuery query = initQuery(source.getQueryId(), p);
        CompiledObject object = initObject(source.getObjectId(), source.getQueryId(), p);
        compiled.setProvider(initDataProvider(compiled, source, context, p, query));
        compiled.setSubmit(initSubmit(source, compiled, object, context, p));
        compiled.setFetchOnInit(castDefault(source.getFetchOnInit(), () -> p.resolve(property("n2o.api.datasource.cached.fetch_on_init"), Boolean.class)));
        return compiled;
    }

    private CachedDatasource.Provider initDataProvider(CachedDatasource compiled, N2oCachedDatasource source,
                                                       CompileContext<?, ?> context,
                                                       CompileProcessor p, CompiledQuery query) {
        CachedDatasource.Provider provider = new CachedDatasource.Provider();
        provider.setCacheExpires(source.getCacheExpires());
        provider.setKey(castDefault(source.getStorageKey(), source.getId()));
        provider.setType("cached");
        provider.setStorage(castDefault(source.getStorageType(),
                () -> p.resolve(property("n2o.api.datasource.cached.storage_type"), BrowserStorageTypeEnum.class)));
        provider.setSize(castDefault(source.getSize(), () -> p.resolve(property("n2o.api.datasource.size"), Integer.class)));

        String datasourceRoute = getDatasourceRoute(source.getId(), compiled.getId(), source.getRoute(), p);
        provider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + datasourceRoute);
        List<Filter> filters = initFilters(source.getId(), source.getFilters(), context, p, query);
        compileRoutes(source.getId(), compiled.getId(), filters, query, p);
        initDataProviderMappings(provider, filters, p);
        p.addRoute(getQueryContext(source.getId(), compiled.getId(), source.getQueryId(),
                source.getSize(), DefaultValuesModeEnum.QUERY, context, p, datasourceRoute, filters, query));

        return provider;
    }

    private void initDataProviderMappings(CachedDatasource.Provider dataProvider, List<Filter> filters, CompileProcessor p) {
        dataProvider.setPathMapping(new HashMap<>());
        dataProvider.setQueryMapping(new LinkedHashMap<>());
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            dataProvider.getPathMapping().putAll(parentRouteScope.getPathMapping());
            dataProvider.getQueryMapping().putAll(parentRouteScope.getQueryMapping());
        }
        if (filters != null) {
            List<String> params = RouteUtil.getParams(dataProvider.getUrl());
            filters.stream()
                    .filter(f -> params.contains(f.getParam()))
                    .forEach(f -> dataProvider.getPathMapping().put(f.getParam(), f.getLink()));
            filters.stream()
                    .filter(f -> !dataProvider.getPathMapping().containsKey(f.getParam()))
                    .forEach(f -> dataProvider.getQueryMapping().put(f.getParam(), f.getLink()));
        }
    }

    private CachedDatasource.Submit initSubmit(N2oCachedDatasource source, CachedDatasource compiled, CompiledObject compiledObject,
                                               CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null) {
            return null;
        }
        N2oClientDataProvider submitProvider = DatasourceCompileStaticProcessor.initSubmit(source.getSubmit(), source.getId(), compiledObject, p);
        submitProvider.setClientDatasourceId(compiled.getId());
        submitProvider.setDatasourceId(source.getId());
        CachedDatasource.Submit submit = new CachedDatasource.Submit();
        compileSubmitClientDataProvider(submitProvider, submit, context, p);
        submit.setClearCache(castDefault(source.getSubmit().getClearCacheAfterSubmit(),
                () -> p.resolve(property("n2o.api.datasource.cached.clear_cache_after_submit"), Boolean.class)));
        submit.setKey(compiled.getProvider().getKey());
        submit.setModel(ReduxModelEnum.RESOLVE);
        submit.setStorage(compiled.getProvider().getStorage());
        return submit;
    }
}
