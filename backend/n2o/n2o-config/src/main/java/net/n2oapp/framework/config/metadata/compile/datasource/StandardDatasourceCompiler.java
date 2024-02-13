package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatoryValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.CopiedFieldScope;
import net.n2oapp.framework.config.metadata.compile.widget.FiltersScope;
import net.n2oapp.framework.config.metadata.compile.widget.SearchBarScope;
import net.n2oapp.framework.config.metadata.compile.widget.SubModelsScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.QueryContextUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceIds;

/**
 * Компиляция источника данных
 */
@Component
public class StandardDatasourceCompiler extends BaseDatasourceCompiler<N2oStandardDatasource, StandardDatasource> {

    public static final String SORTING = "sorting.";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardDatasource.class;
    }

    @Override
    public StandardDatasource compile(N2oStandardDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardDatasource compiled = new StandardDatasource();

        compileDatasource(source, compiled, p);
        initDefaults(source);
        compiled.setDefaultValuesMode(castDefault(
                source.getDefaultValuesMode(),
                source.getQueryId() == null
                        ? DefaultValuesMode.defaults
                        : DefaultValuesMode.query
        ));
        CompiledQuery query = initQuery(source, p);
        CompiledObject object = initObject(source, p);
        compiled.setProvider(initDataProvider(compiled, source, context, p, query, compiled.getDefaultValuesMode()));
        compiled.setSubmit(initSubmit(source, compiled, object, context, p));
        compiled.setQueryId(source.getQueryId());

        return compiled;
    }

    private CompiledQuery initQuery(N2oStandardDatasource source, CompileProcessor p) {
        return source.getQueryId() != null
                ? p.getCompiled(new QueryContext(source.getQueryId()))
                : null;
    }

    private CompiledObject initObject(N2oStandardDatasource source, CompileProcessor p) {
        if (source.getObjectId() != null) {
            return p.getCompiled(new ObjectContext(source.getObjectId()));
        } else if (source.getQueryId() != null) {
            return p.getCompiled(new QueryContext(source.getQueryId())).getObject();
        }

        return null;
    }

    private void initDefaults(N2oStandardDatasource source) {
        source.setDefaultValuesMode(
                castDefault(
                        source.getDefaultValuesMode(),
                        source.getQueryId() != null ? DefaultValuesMode.query : DefaultValuesMode.defaults
                )
        );
    }

    private ClientDataProvider initDataProvider(StandardDatasource compiled, N2oStandardDatasource source, CompileContext<?, ?> context,
                                                CompileProcessor p, CompiledQuery query, DefaultValuesMode defaultValuesMode) {
        if (source.getQueryId() == null)
            return null;

        ClientDataProvider dataProvider = new ClientDataProvider();
        String url = getDatasourceRoute(source, compiled, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + url);
        dataProvider.setSize(castDefault(
                source.getSize(),
                () -> p.resolve(property("n2o.api.datasource.size"), Integer.class)
        ));
        List<Filter> filters = initFilters(source, context, p, query);
        compileRoutes(compiled, source, filters, p, query);
        initDataProviderMappings(dataProvider, filters, p);
        p.addRoute(getQueryContext(compiled, source, context, p, url, filters, query));

        return defaultValuesMode == DefaultValuesMode.defaults ? null : dataProvider;
    }

    private void initSearchBar(String datasourceId, List<Filter> filters, CompileProcessor p) {
        SearchBarScope searchBarScope = p.getScope(SearchBarScope.class);

        if (searchBarScope == null || searchBarScope.getDatasource() == null || !searchBarScope.getDatasource().equals(datasourceId)) {
            return;
        }
        if (filters.stream().anyMatch(
                f -> f.getFilterId().equals(searchBarScope.getFilterId()) || f.getParam().equals(searchBarScope.getParam())
        )) {
            return;
        }

        Filter searchBarFilter = new Filter();

        searchBarFilter.setFilterId(searchBarScope.getFilterId());
        searchBarFilter.setParam(searchBarScope.getParam());
        searchBarFilter.setRoutable(true);
        ModelLink modelLink = new ModelLink(
                searchBarScope.getModelPrefix(),
                getClientDatasourceId(datasourceId, p),
                searchBarScope.getFilterId()
        );
        searchBarFilter.setLink(modelLink);

        filters.add(searchBarFilter);
    }

    private String getDatasourceRoute(N2oStandardDatasource source, StandardDatasource compiled, CompileProcessor p) {
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        String datasource = parentRouteScope != null && "/".equals(parentRouteScope.getUrl())
                ? compiled.getId()
                : source.getId();
        String route = castDefault(source.getRoute(), () -> normalize(datasource));

        return parentRouteScope != null
                ? RouteUtil.normalize(parentRouteScope.getUrl() + route)
                : route;
    }

    private void initDataProviderMappings(ClientDataProvider dataProvider, List<Filter> filters, CompileProcessor p) {
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

    private List<Filter> initFilters(N2oStandardDatasource source, CompileContext<?, ?> context, CompileProcessor p, CompiledQuery query) {
        if (query == null) {
            return null;
        }
        List<Filter> filters = new ArrayList<>();

        if (source.getFilters() != null) {
            for (N2oPreFilter preFilter : source.getFilters()) {
                N2oQuery.Filter queryFilter = query.getFilterByPreFilter(preFilter);

                if (queryFilter == null) {
                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
                }

                Filter filter = new Filter();
                initMandatoryValidation(source.getId(), context, p, preFilter, queryFilter);
                filter.setParam(castDefault(preFilter.getParam(), () -> source.getId() + "_" + queryFilter.getParam()));
                filter.setRoutable(castDefault(preFilter.getRoutable(), false));
                filter.setFilterId(queryFilter.getFilterId());
                filter.setLink(getFilterModelLink(source.getId(), preFilter, filter.getParam(), p));
                if (preFilter.getRequired() != null) {
                    filter.getLink().setRequired(preFilter.getRequired());
                }
                filters.add(filter);
            }
        }
        initSearchBar(source.getId(), filters, p);
        initFiltersScope(source.getId(), filters, p);

        return filters;
    }

    private ModelLink getFilterModelLink(String datasourceId, N2oPreFilter preFilter, String filterParam, CompileProcessor p) {
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        if (routeScope != null && routeScope.getQueryMapping() != null && routeScope.getQueryMapping().containsKey(filterParam)) {
            //фильтр из родительского маршрута
            return routeScope.getQueryMapping().get(filterParam);
        }

        Object prefilterValue = getPrefilterValue(preFilter);
        if (StringUtils.isJs(prefilterValue)) {
            String clientDatasourceId = preFilter.getRefPageId() != null
                    ? getClientDatasourceId(preFilter.getDatasourceId(), preFilter.getRefPageId(), p)
                    : getClientDatasourceId(preFilter.getDatasourceId(), p);
            ReduxModel model = castDefault(preFilter.getModel(), ReduxModel.resolve);
            ModelLink link = new ModelLink(
                    model,
                    clientDatasourceId != null ? clientDatasourceId : getClientDatasourceId(datasourceId, p)
            );
            link.setValue(prefilterValue);
            link.setParam(filterParam);

            return link;
        }
        //фильтр с константным значением или значением из параметра в url
        ModelLink link = new ModelLink(prefilterValue);
        link.setParam(filterParam);

        return link;
    }

    private void initFiltersScope(String datasourceId, List<Filter> filters, CompileProcessor p) {
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        if (filtersScope == null) {
            return;
        }

        Map<String, Filter> filterMap = filters.stream()
                .collect(Collectors.toMap(Filter::getFilterId, f -> f, (f1, f2) -> f2));
        filtersScope.getFilters(datasourceId)
                .stream()
                .filter(f -> !filterMap.containsKey(f.getFilterId()))
                .forEach(filters::add);
    }

    private void initMandatoryValidation(String datasourceId, CompileContext<?, ?> context, CompileProcessor p,
                                         N2oPreFilter preFilter, N2oQuery.Filter queryFilter) {
        if (!Boolean.TRUE.equals(preFilter.getRequired()) || p.getScope(ValidationScope.class) == null) {
            return;
        }

        N2oMandatoryValidation n2oMandatoryValidation = new N2oMandatoryValidation();
        n2oMandatoryValidation.setId(queryFilter.getFilterId());
        n2oMandatoryValidation.setMessage(p.getMessage("n2o.required.filter"));
        n2oMandatoryValidation.setFieldId(queryFilter.getFilterId());
        n2oMandatoryValidation.setServerMoment(N2oValidation.ServerMoment.beforeQuery);
        n2oMandatoryValidation.setSeverity(SeverityType.danger);
        MandatoryValidation v = p.compile(n2oMandatoryValidation, context);

        ValidationScope validationScope = p.getScope(ValidationScope.class);
        validationScope.add(datasourceId, ReduxModel.filter, v);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        }

        return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
    }

    private QueryContext getQueryContext(StandardDatasource compiled,
                                         N2oStandardDatasource source,
                                         CompileContext<?, ?> context,
                                         CompileProcessor p,
                                         String route,
                                         List<Filter> filters,
                                         CompiledQuery query) {
        QueryContext queryContext = new QueryContext(source.getQueryId(), route, context.getUrlPattern());

        ValidationScope validationScope = p.getScope(ValidationScope.class);
        List<Validation> validations = validationScope == null ? null : validationScope.get(source.getId(), ReduxModel.filter);
        queryContext.setValidations(validations);
        queryContext.setFilters(filters);
        if (source.getDefaultValuesMode() != null) {
            queryContext.setMode(source.getDefaultValuesMode());
        }
        queryContext.setMessagesForm(compiled.getId());
        queryContext.setQuerySize(source.getSize());
        queryContext.setSortingMap(QueryContextUtil.initSortingMap(query));

        SubModelsScope subModelsScope = p.getScope(SubModelsScope.class);
        if (subModelsScope != null) {
            queryContext.setSubModelQueries(subModelsScope.get(source.getId()));
        }

        CopiedFieldScope copiedFieldScope = p.getScope(CopiedFieldScope.class);
        if (copiedFieldScope != null) {
            queryContext.setCopiedFields(copiedFieldScope.getCopiedFields(source.getId()));
        }

        return queryContext;
    }

    private ClientDataProvider initSubmit(N2oStandardDatasource source, StandardDatasource compiled, CompiledObject compiledObject,
                                          CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null) {
            return null;
        }
        N2oClientDataProvider submitProvider = initSubmit(source.getSubmit(), source.getId(), compiledObject, p);

        submitProvider.setSubmitForm(castDefault(source.getSubmit().getSubmitAll(), true));
        submitProvider.setClientDatasourceId(compiled.getId());
        submitProvider.setDatasourceId(source.getId());
        submitProvider.getActionContextData().setMessagesForm(source.getSubmit().getMessageWidgetId());

        return compileSubmit(submitProvider, context, p);
    }


    private N2oClientDataProvider initSubmit(Submit submit, String datasourceId, CompiledObject object, CompileProcessor p) {
        if (object == null) {
            throw new N2oException(String.format("For compilation submit for datasource [%s] is necessary object!", datasourceId));
        }

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setMethod(RequestMethod.POST);
        dataProvider.setUrl(submit.getRoute());
        dataProvider.setTargetModel(ReduxModel.resolve);
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());
        dataProvider.setAutoSubmitOn(submit.getSubmitOn());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(object.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(submit.getRoute());
        actionContextData.setMessageOnSuccess(castDefault(submit.getMessageOnSuccess(),
                () -> p.resolve(property("n2o.api.datasource.submit.message_on_success"), Boolean.class)));
        actionContextData.setMessageOnFail(castDefault(submit.getMessageOnFail(),
                () -> p.resolve(property("n2o.api.datasource.submit.message_on_fail"), Boolean.class)));
        actionContextData.setMessagePosition(submit.getMessagePosition());
        actionContextData.setMessagePlacement(submit.getMessagePlacement());
        actionContextData.setOperation(object.getOperations().get(submit.getOperationId()));
        if (submit.getRefreshOnSuccess() != null) {
            actionContextData.setRefresh(new RefreshSaga());
            if (submit.getRefreshDatasourceIds() != null) {
                actionContextData.getRefresh().setDatasources(getClientDatasourceIds(Arrays.asList(submit.getRefreshDatasourceIds()), p));
            }
        }
        dataProvider.setActionContextData(actionContextData);

        return dataProvider;
    }

    private ClientDataProvider compileSubmit(N2oClientDataProvider source, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String clientDatasourceId = source.getClientDatasourceId();
        ReduxModel targetModel = initTargetWidgetModel(p, source.getTargetModel());

        Map<String, ModelLink> pathMapping = new HashMap<>(compileParams(source.getPathParams(), context, targetModel, clientDatasourceId));
        dataProvider.setFormMapping(compileParams(source.getFormParams(), context, targetModel, clientDatasourceId));
        dataProvider.setHeadersMapping(compileParams(source.getHeaderParams(), context, targetModel, clientDatasourceId));
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = castDefault(routeScope != null ? routeScope.getUrl() : null, () -> context.getRoute((N2oCompileProcessor) p), () -> "");
        if (context.getPathRouteMapping() != null) {
            pathMapping.putAll(context.getPathRouteMapping());
        }
        path = normalize(path + normalize(castDefault(source.getUrl(), source.getDatasourceId())));
        dataProvider.setPathMapping(pathMapping);
        dataProvider.setMethod(source.getMethod());
        dataProvider.setOptimistic(source.getOptimistic());
        dataProvider.setSubmitForm(source.getSubmitForm());

        ClientDataProviderUtil.initActionContext(source, pathMapping, path, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + castDefault(path, ""));
        dataProvider.setQueryMapping(compileParams(source.getQueryParams(), context, targetModel, clientDatasourceId));
        dataProvider.setQuickSearchParam(source.getQuickSearchParam());
        dataProvider.setSize(source.getSize());
        dataProvider.setAutoSubmitOn(source.getAutoSubmitOn());

        return dataProvider;
    }

    private ReduxModel initTargetWidgetModel(CompileProcessor p, ReduxModel defaultModel) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);

        if (componentScope == null) {
            return defaultModel;
        }

        ModelAware modelAware = componentScope.unwrap(ModelAware.class);
        if (modelAware != null && modelAware.getModel() != null) {
            return modelAware.getModel();
        }

        return defaultModel;
    }

    private Map<String, ModelLink> compileParams(N2oParam[] params, CompileContext<?, ?> context,
                                                 ReduxModel model, String clientDatasourceId) {
        if (params == null) {
            return Collections.emptyMap();
        }

        Map<String, ModelLink> result = new HashMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            link = param.getValueParam() == null
                    ? getModelLink(model, clientDatasourceId, param)
                    : getModelLinkByParam(context, param);
            result.put(param.getName(), link);
        }

        return result;
    }

    private ModelLink getModelLink(ReduxModel model, String clientDatasourceId, N2oParam param) {
        Object value = param.getValueList() != null
                ? param.getValueList()
                : ScriptProcessor.resolveExpression(param.getValue());

        if (value != null && !StringUtils.isJs(value)) {
            return new ModelLink(value);
        }

        ModelLink link = new ModelLink(castDefault(param.getModel(), model), clientDatasourceId);
        link.setValue(value);

        return link;
    }

    private ModelLink getModelLinkByParam(CompileContext<?, ?> context, N2oParam param) {
        ModelLink link;
        if (context.getPathRouteMapping() != null && context.getPathRouteMapping().containsKey(param.getValueParam())) {
            link = context.getPathRouteMapping().get(param.getValueParam());
            link.setParam(param.getValueParam());
        } else if (context.getQueryRouteMapping() != null && context.getQueryRouteMapping().containsKey(param.getValueParam())) {
            link = context.getQueryRouteMapping().get(param.getValueParam());
            link.setParam(param.getValueParam());
        } else {
            link = new ModelLink();
            link.setParam(param.getValueParam());
        }

        return link;
    }

    private void compileRoutes(StandardDatasource compiled, N2oStandardDatasource source, List<Filter> filters,
                               CompileProcessor p, CompiledQuery query) {
        PageRoutes routes = p.getScope(PageRoutes.class);
        if (routes == null) {
            return;
        }
        if (query == null) {
            return;
        }

        filters.stream()
                .filter(Filter::getRoutable)
                .filter(f -> !f.getLink().isConst())
                .forEach(filter -> routes.addQueryMapping(filter.getParam(), null, filter.getLink()));
        for (QuerySimpleField field : query.getSortingFields()) {
            String sortParam = RouteUtil.normalizeParam(SORTING + source.getId() + "_" + field.getId());
            BindLink onSet = Redux.createSortLink(compiled.getId(), field.getId());
            ReduxAction onGet = Redux.dispatchSortWidget(compiled.getId(), field.getId(), colon(sortParam));
            routes.addQueryMapping(sortParam, onGet, onSet);
        }
    }
}
