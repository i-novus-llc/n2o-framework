package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
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
 * Компиляция источника данных
 */
@Component
public class StandardDatasourceCompiler extends BaseDatasourceCompiler<N2oStandardDatasource, StandardDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardDatasource.class;
    }

    @Override
    public StandardDatasource compile(N2oStandardDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardDatasource compiled = new StandardDatasource();

        compileDatasource(source, compiled, p);
        compiled.setDefaultValuesMode(castDefault(
                source.getDefaultValuesMode(),
                source.getQueryId() == null
                        ? DefaultValuesMode.defaults
                        : DefaultValuesMode.query
        ));
        CompiledQuery query = initQuery(source.getQueryId(), p);
        CompiledObject object = initObject(source.getObjectId(), source.getQueryId(), p);
        compiled.setProvider(initDataProvider(compiled, source, context, p, query, compiled.getDefaultValuesMode()));
        compiled.setSubmit(initSubmit(source, compiled, object, context, p));
        compiled.setQueryId(source.getQueryId());

        return compiled;
    }

    private ClientDataProvider initDataProvider(StandardDatasource compiled, N2oStandardDatasource source, CompileContext<?, ?> context,
                                                CompileProcessor p, CompiledQuery query, DefaultValuesMode defaultValuesMode) {
        if (source.getQueryId() == null)
            return null;

        ClientDataProvider dataProvider = new ClientDataProvider();
        String url = getDatasourceRoute(source.getId(), compiled.getId(), source.getRoute(), p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + url);
        dataProvider.setSize(castDefault(source.getSize(), () -> p.resolve(property("n2o.api.datasource.size"), Integer.class)));
        List<Filter> filters = initFilters(source.getId(), source.getFilters(), context, p, query);
        compileRoutes(source.getId(), compiled.getId(), filters, query, p);
        initDataProviderMappings(dataProvider, filters, source, p);
        p.addRoute(getQueryContext(source.getId(), compiled.getId(), source.getQueryId(),
                source.getSize(), source.getDefaultValuesMode(), context, p, url, filters, query));
        return defaultValuesMode == DefaultValuesMode.defaults ? null : dataProvider;
    }

    private void initDataProviderMappings(ClientDataProvider dataProvider, List<Filter> filters, N2oStandardDatasource source, CompileProcessor p) {
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
        if (source.getAdditionalQueryMapping() != null) {
            dataProvider.getQueryMapping().putAll(source.getAdditionalQueryMapping());
        }
    }

    private ClientDataProvider initSubmit(N2oStandardDatasource source, StandardDatasource compiled, CompiledObject compiledObject,
                                          CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null) {
            return null;
        }
        N2oClientDataProvider submitProvider = DatasourceCompileStaticProcessor.initSubmit(source.getSubmit(), source.getId(), compiledObject, p);
        submitProvider.setClientDatasourceId(compiled.getId());
        submitProvider.setDatasourceId(source.getId());
        ClientDataProvider result = new ClientDataProvider();
        compileSubmitClientDataProvider(submitProvider, result, context, p);
        return result;
    }
}
