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
import net.n2oapp.framework.api.metadata.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
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
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceIds;

/**
 * Компиляция источника данных
 */
@Component
public class StandardDatasourceCompiler extends BaseDatasourceCompiler<N2oStandardDatasource, StandardDatasource> {
    private static final String SPREAD_OPERATOR = "*.";
    public static final String SORTING = "sorting.";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardDatasource.class;
    }

    @Override
    public StandardDatasource compile(N2oStandardDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardDatasource compiled = new StandardDatasource();
        compileDatasource(source, compiled, p);
        initDefaults(source, p);
        compiled.setDefaultValuesMode(p.cast(source.getDefaultValuesMode(), source.getQueryId() == null ?
                DefaultValuesMode.defaults : DefaultValuesMode.query));
        CompiledQuery query = initQuery(source, p);
        CompiledObject object = initObject(source, p);
        compiled.setProvider(initDataProvider(compiled, source, context, p, query, compiled.getDefaultValuesMode()));
        compiled.setSubmit(initSubmit(source, compiled, object, context, p));
        compiled.setQueryId(source.getQueryId());
        return compiled;
    }

    private CompiledQuery initQuery(N2oStandardDatasource source, CompileProcessor p) {
        return source.getQueryId() != null ? p.getCompiled(new QueryContext(source.getQueryId())) : null;
    }

    private CompiledObject initObject(N2oStandardDatasource source, CompileProcessor p) {
        if (source.getObjectId() != null) {
            return p.getCompiled(new ObjectContext(source.getObjectId()));
        } else if (source.getQueryId() != null) {
            CompiledQuery query = p.getCompiled(new QueryContext(source.getQueryId()));
            return query.getObject();
        }
        return null;
    }

    private void initDefaults(N2oStandardDatasource source, CompileProcessor p) {
        source.setDefaultValuesMode(p.cast(source.getDefaultValuesMode(), source.getQueryId() != null ? DefaultValuesMode.query : DefaultValuesMode.defaults));
    }

    private ClientDataProvider initDataProvider(StandardDatasource compiled, N2oStandardDatasource source, CompileContext<?, ?> context,
                                                CompileProcessor p, CompiledQuery query, DefaultValuesMode defaultValuesMode) {
        if (source.getQueryId() == null)
            return null;
        ClientDataProvider dataProvider = new ClientDataProvider();
        String url = getDatasourceRoute(source, compiled, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + url);
        dataProvider.setSize(p.cast(source.getSize(),
                p.resolve(property("n2o.api.datasource.size"), Integer.class)));
        List<Filter> filters = initFilters(source, p, query);
        compileRoutes(compiled, source, filters, p, query);
        initDataProviderMappings(dataProvider, filters, p);
        p.addRoute(getQueryContext(compiled, source, context, p, url, filters, query));
        return defaultValuesMode == DefaultValuesMode.defaults ? null : dataProvider;
    }

    private void initSearchBar(N2oStandardDatasource source, List<Filter> filters, CompileProcessor p) {
        SearchBarScope searchBarScope = p.getScope(SearchBarScope.class);
        if (searchBarScope != null && searchBarScope.getDatasource() != null &&
                searchBarScope.getDatasource().equals(source.getId())) {
            if (filters.stream().noneMatch(f -> f.getFilterId().equals(searchBarScope.getFilterId())
                    || f.getParam().equals(searchBarScope.getParam()))) {
                Filter searchBarFilter = new Filter();
                searchBarFilter.setFilterId(searchBarScope.getFilterId());
                searchBarFilter.setParam(searchBarScope.getParam());
                searchBarFilter.setRoutable(true);
                ModelLink modelLink = new ModelLink(searchBarScope.getModelPrefix(),
                        getClientDatasourceId(source.getId(), p),
                        searchBarScope.getFilterId());
                searchBarFilter.setLink(modelLink);
                filters.add(searchBarFilter);
            }
        }
    }

    private String getDatasourceRoute(N2oStandardDatasource source, StandardDatasource compiled, CompileProcessor p) {
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        String datasource = parentRouteScope != null && "/".equals(parentRouteScope.getUrl()) ? compiled.getId() : source.getId();
        String route = p.cast(source.getRoute(), normalize(datasource));

        return parentRouteScope != null ?
                RouteUtil.normalize(parentRouteScope.getUrl() + route) :
                route;
    }

    private void initDataProviderMappings(ClientDataProvider dataProvider, List<Filter> filters, CompileProcessor p) {
        dataProvider.setPathMapping(new StrictMap<>());
        dataProvider.setQueryMapping(new StrictMap<>());
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            dataProvider.getPathMapping().putAll(parentRouteScope.getPathMapping());
            dataProvider.getQueryMapping().putAll(parentRouteScope.getQueryMapping());
        }
        if (filters != null) {
            List<String> params = RouteUtil.getParams(dataProvider.getUrl());
            filters.stream().filter(f -> params.contains(f.getParam()))
                    .forEach(f -> dataProvider.getPathMapping().put(f.getParam(), f.getLink()));
            filters.stream().filter(f -> !dataProvider.getPathMapping().containsKey(f.getParam()))
                    .forEach(f -> dataProvider.getQueryMapping().put(f.getParam(), f.getLink()));
        }
    }

    private List<Filter> initFilters(N2oStandardDatasource source, CompileProcessor p, CompiledQuery query) {
        if (query == null)
            return null;
        List<Filter> filters = new ArrayList<>();
        if (source.getFilters() != null) {
            for (N2oPreFilter preFilter : source.getFilters()) {
                N2oQuery.Filter queryFilter = query.getFilterByPreFilter(preFilter);
                if (queryFilter != null) {
                    Filter filter = new Filter();
                    initMandatoryValidation(source, p, preFilter, queryFilter);
                    filter.setParam(p.cast(preFilter.getParam(), source.getId() + "_" + queryFilter.getParam()));
                    filter.setRoutable(p.cast(preFilter.getRoutable(), false));
                    filter.setFilterId(queryFilter.getFilterId());
                    Object prefilterValue = getPrefilterValue(preFilter);
                    ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
                    if (routeScope != null && routeScope.getQueryMapping() != null && routeScope.getQueryMapping().containsKey(filter.getParam())) {
                        //фильтр из родительского маршрута
                        filter.setLink(routeScope.getQueryMapping().get(filter.getParam()));
                    } else if (StringUtils.isJs(prefilterValue)) {
                        String clientDatasourceId = preFilter.getRefPageId() != null ?
                                getClientDatasourceId(preFilter.getDatasourceId(), preFilter.getRefPageId(), p) :
                                getClientDatasourceId(preFilter.getDatasourceId(), p);
                        ReduxModel model = p.cast(preFilter.getModel(), ReduxModel.resolve);
                        ModelLink link = new ModelLink(
                                model,
                                clientDatasourceId != null ? clientDatasourceId : getClientDatasourceId(source.getId(), p));
                        link.setValue(prefilterValue);
                        link.setParam(filter.getParam());
                        filter.setLink(link);
                    } else {
                        //фильтр с константным значением или значением из параметра в url
                        ModelLink link = new ModelLink(prefilterValue);
                        link.setParam(filter.getParam());
                        filter.setLink(link);
                    }
                    if (preFilter.getRequired() != null)
                        filter.getLink().setRequired(preFilter.getRequired());
                    filters.add(filter);
                } else {
                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
                }
            }
        }
        initSearchBar(source, filters, p);
        initFiltersScope(source, filters, p);
        return filters;
    }

    private void initFiltersScope(N2oStandardDatasource source, List<Filter> filters, CompileProcessor p) {
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        if (filtersScope == null) return;
        Map<String, Filter> filterMap = filters.stream().collect(Collectors.toMap(Filter::getFilterId, f -> f, (f1, f2) -> f2));
        filtersScope.getFilters(source.getId()).stream().filter(f -> !filterMap.containsKey(f.getFilterId())).forEach(filters::add);
    }

    private void initMandatoryValidation(N2oStandardDatasource source, CompileProcessor p,
                                         N2oPreFilter preFilter, N2oQuery.Filter queryFilter) {
        if (preFilter.getRequired() != null && preFilter.getRequired()) {
            if (p.getScope(ValidationScope.class) != null) {
                MandatoryValidation v = new MandatoryValidation(
                        queryFilter.getFilterId(),
                        p.getMessage("n2o.required.filter"),
                        queryFilter.getFilterId()
                );
                v.setMoment(N2oValidation.ServerMoment.beforeQuery);
                v.setSeverity(SeverityType.danger);

                ValidationScope validationScope = p.getScope(ValidationScope.class);
                validationScope.add(source.getId(), ReduxModel.filter, v);
            }
        }
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
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
        if (source.getDefaultValuesMode() != null)
            queryContext.setUpload(UploadType.values()[source.getDefaultValuesMode().ordinal()]);
        queryContext.setMessagesForm(compiled.getId());
        SubModelsScope subModelsScope = p.getScope(SubModelsScope.class);
        if (subModelsScope != null) {
            queryContext.setSubModelQueries(subModelsScope.get(source.getId()));
        }
        queryContext.setQuerySize(source.getSize());
        CopiedFieldScope copiedFieldScope = p.getScope(CopiedFieldScope.class);
        if (copiedFieldScope != null)
            queryContext.setCopiedFields(copiedFieldScope.getCopiedFields(source.getId()));
        queryContext.setSortingMap(initSortingMap(query));
        return queryContext;
    }

    private Map<String, String> initSortingMap(CompiledQuery query) {
        Map<String, String> sortingMap = new HashMap<>();
        for (QuerySimpleField sortingField : query.getSortingFields()) {
            sortingMap.put(SORTING + RouteUtil.normalizeParam(sortingField.getId()), sortingField.getId());
        }
        return sortingMap;
    }

    private ClientDataProvider initSubmit(N2oStandardDatasource source, StandardDatasource compiled, CompiledObject compiledObject,
                                          CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        N2oClientDataProvider submitProvider = initSubmit(source.getSubmit(), source.getId(), compiledObject, p);

        submitProvider.setSubmitForm(p.cast(source.getSubmit().getSubmitAll(), true));
        submitProvider.setClientDatasourceId(compiled.getId());
        submitProvider.setDatasourceId(source.getId());
        submitProvider.getActionContextData().setMessagesForm(source.getSubmit().getMessageWidgetId());
        return compileSubmit(submitProvider, context, p);
    }


    private N2oClientDataProvider initSubmit(Submit submit, String datasourceId, CompiledObject object, CompileProcessor p) {
        if (object == null)
            throw new N2oException(String.format("For compilation submit for datasource [%s] is necessary object!", datasourceId));

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
        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(),
                p.resolve(property("n2o.api.datasource.submit.message_on_success"), Boolean.class)));
        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(),
                p.resolve(property("n2o.api.datasource.submit.message_on_fail"), Boolean.class)));
        actionContextData.setMessagePosition(submit.getMessagePosition());
        actionContextData.setMessagePlacement(submit.getMessagePlacement());
        actionContextData.setOperation(object.getOperations().get(submit.getOperationId()));
        if (submit.getRefreshOnSuccess() != null) {
            actionContextData.setRefresh(new RefreshSaga());
            if (submit.getRefreshDatasourceIds() != null)
                actionContextData.getRefresh().setDatasources(getClientDatasourceIds(Arrays.asList(submit.getRefreshDatasourceIds()), p));
        }
        dataProvider.setActionContextData(actionContextData);

        return dataProvider;
    }

    private ClientDataProvider compileSubmit(N2oClientDataProvider source, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String clientDatasourceId = source.getClientDatasourceId();
        ReduxModel targetModel = initTargetWidgetModel(p, source.getTargetModel());

        Map<String, ModelLink> pathMapping = new StrictMap<>();
        pathMapping.putAll(compileParams(source.getPathParams(), context, p, targetModel, clientDatasourceId));
        dataProvider.setFormMapping(compileParams(source.getFormParams(), context, p, targetModel, clientDatasourceId));
        dataProvider.setHeadersMapping(compileParams(source.getHeaderParams(), context, p, targetModel, clientDatasourceId));
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");
        if (context.getPathRouteMapping() != null)
            pathMapping.putAll(context.getPathRouteMapping());
        path = normalize(path + normalize(p.cast(source.getUrl(), source.getDatasourceId())));
        dataProvider.setPathMapping(pathMapping);
        dataProvider.setMethod(source.getMethod());
        dataProvider.setOptimistic(source.getOptimistic());
        dataProvider.setSubmitForm(source.getSubmitForm());


        ClientDataProviderUtil.initActionContext(source, pathMapping, path, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + p.cast(path, ""));
        dataProvider.setQueryMapping(compileParams(source.getQueryParams(), context, p, targetModel, clientDatasourceId));
        dataProvider.setQuickSearchParam(source.getQuickSearchParam());
        dataProvider.setSize(source.getSize());
        dataProvider.setAutoSubmitOn(source.getAutoSubmitOn());

        return dataProvider;
    }

    private ReduxModel initTargetWidgetModel(CompileProcessor p, ReduxModel defaultModel) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null)
                return modelAware.getModel();
        }
        return defaultModel;
    }

    private Map<String, ModelLink> compileParams(N2oParam[] params, CompileContext<?, ?> context,
                                                 CompileProcessor p, ReduxModel model, String clientDatasourceId) {
        if (params == null)
            return Collections.emptyMap();
        Map<String, ModelLink> result = new StrictMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            if (param.getValueParam() == null) {
                link = getModelLink(p, model, clientDatasourceId, param);
            } else {
                link = getModelLinkByParam(context, param);
            }
            result.put(param.getName(), link);
        }
        return result;
    }

    private ModelLink getModelLink(CompileProcessor p, ReduxModel model, String clientDatasourceId, N2oParam param) {
        ModelLink link;
        Object value = param.getValueList() != null ? param.getValueList() :
                ScriptProcessor.resolveExpression(param.getValue());
        if (value == null || StringUtils.isJs(value)) {
            link = new ModelLink(p.cast(param.getModel(), model), clientDatasourceId);
            link.setValue(value);
        } else {
            link = new ModelLink(value);
        }
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
        if (routes == null)
            return;
        if (query != null) {
            filters.stream()
                    .filter(Filter::getRoutable)
                    .filter(f -> !f.getLink().isConst())
                    .forEach(filter -> {
                        routes.addQueryMapping(filter.getParam(), null, filter.getLink());
                    });
            for (QuerySimpleField field : query.getSortingFields()) {
                String sortParam = RouteUtil.normalizeParam(SORTING + source.getId() + "_" + field.getId());
                BindLink onSet = Redux.createSortLink(compiled.getId(), field.getId());
                ReduxAction onGet = Redux.dispatchSortWidget(compiled.getId(), field.getId(), colon(sortParam));
                routes.addQueryMapping(sortParam, onGet, onSet);
            }
        }
    }

}
