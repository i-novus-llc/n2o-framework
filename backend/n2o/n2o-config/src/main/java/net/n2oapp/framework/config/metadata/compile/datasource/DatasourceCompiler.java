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
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.CompileUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

@Component
public class DatasourceCompiler implements BaseSourceCompiler<Datasource, N2oDatasource, CompileContext<?, ?>> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatasource.class;
    }

    @Override
    public Datasource compile(N2oDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        Datasource compiled = new Datasource();
        PageScope pageScope = p.getScope(PageScope.class);
        compiled.setId(pageScope.getClientDatasourceId(source.getId()));
        compiled.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.widget.table.size"), Integer.class)));
        if (source.getQueryId() != null)
            compiled.setDefaultValuesMode(DefaultValuesMode.query);
        else
            compiled.setDefaultValuesMode(p.cast(source.getDefaultValuesMode(), DefaultValuesMode.defaults));
        CompiledObject object = null;
        if (source.getObjectId() != null) {
            object = p.getCompiled(new ObjectContext(source.getObjectId()));
        } else if (source.getQueryId() != null) {
            CompiledQuery query = p.getCompiled(new QueryContext(source.getQueryId()));
            object = query.getObject();
        }
        compiled.setProvider(initDataProvider(compiled, source, context, p));
        compiled.setValidations(initValidations(source, p));
        compiled.setSubmit(initSubmit(source, compiled, object, context, p));
        compiled.setDependencies(initDependencies(source, p));
        return compiled;
    }

    private List<DependencyCondition> initDependencies(N2oDatasource source, CompileProcessor p) {
        List<DependencyCondition> fetch = new ArrayList<>();
        PageScope pageScope = p.getScope(PageScope.class);
        String pageId = pageScope.getPageId();
        if (source.getDependencies() != null) {
            for (N2oDatasource.Dependency d : source.getDependencies()) {
                if (d instanceof N2oDatasource.FetchDependency) { //fixme учесть model из xml
                    ModelLink bindLink = new ModelLink(ReduxModel.RESOLVE,
                            CompileUtil.generateWidgetId(pageId, ((N2oDatasource.FetchDependency) d).getOn()));
                    DependencyCondition condition = new DependencyCondition();
                    condition.setOn(bindLink.getBindLink());
                    fetch.add(condition);
                }
            }
        }
        return fetch;
    }

    private Map<String, List<Validation>> initValidations(N2oDatasource source, CompileProcessor p) {
        ValidationList validationList = p.getScope(ValidationList.class);
        if (validationList != null) {
            //todo why RESOLVE ?
            return validationList.get(source.getId(), ReduxModel.RESOLVE).stream()
                    .filter(v -> v.getSide() == null || v.getSide().contains("client"))
                    .collect(Collectors.groupingBy(Validation::getFieldId));
        } else
            return Collections.emptyMap();
    }

    private ClientDataProvider initDataProvider(Datasource compiled, N2oDatasource source, CompileContext<?, ?> context,
                                                CompileProcessor p) {
        if (source.getQueryId() == null)
            return null;
        ClientDataProvider dataProvider = new ClientDataProvider();
        String url = getDatasourceRoute(source, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + url);
        dataProvider.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.datasource.size"), Integer.class)));
        List<Filter> filters = initFilters(compiled, source, p);
        initDataProviderMappings(compiled, source, dataProvider, filters, p);
        SearchBarScope searchBarScope = p.getScope(SearchBarScope.class);
        if (searchBarScope != null && searchBarScope.getWidgetId().equals(source.getId())) {
            PageScope pageScope = p.getScope(PageScope.class);
            String searchWidgetId = pageScope != null ?
                    CompileUtil.generateWidgetId(pageScope.getPageId(), searchBarScope.getWidgetId()) :
                    searchBarScope.getWidgetId();
            ModelLink modelLink = new ModelLink(searchBarScope.getModelPrefix(),
                    pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null ?
                            searchWidgetId : pageScope.getWidgetIdClientDatasourceMap().get(searchWidgetId));
            modelLink.setFieldValue(searchBarScope.getModelKey());
            dataProvider.getQueryMapping().put(searchBarScope.getModelKey(), modelLink);
//            if (!datasource.containsFilter(searchBarScope.getModelKey())) { fixme
//                if (datasource.getFilters() == null) datasource.setFilters(new ArrayList<>());
//                Filter filter = new Filter();
//                filter.setFilterId(searchBarScope.getModelKey());
//                filter.setLink(modelLink);
//                filter.setRoutable(false);
//                datasource.getFilters().add(filter);
//            }
        }
        p.addRoute(getQueryContext(compiled, source, context, p, url, filters));
        return dataProvider;
    }

    private String getDatasourceRoute(N2oDatasource source, CompileProcessor p) {
        String datasource = source.getId();
        String route = p.cast(source.getRoute(), normalize(datasource));
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            return RouteUtil.normalize(parentRouteScope.getUrl() + route);
        } else {
            return route;
        }
    }

    private void initDataProviderMappings(Datasource compiled, N2oDatasource source, ClientDataProvider dataProvider,
                                          List<Filter> filters, CompileProcessor p) {
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

    private List<Filter> initFilters(Datasource compiled, N2oDatasource source, CompileProcessor p) {
        CompiledQuery query = source.getQueryId() == null ? null : p.getCompiled(new QueryContext(source.getQueryId()));
        PageScope pageScope = p.getScope(PageScope.class);
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
                    filter.setFilterId(queryFilter.getFilterField());
                    Object prefilterValue = getPrefilterValue(preFilter);
                    ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
                    if (routeScope != null && routeScope.getQueryMapping() != null && routeScope.getQueryMapping().containsKey(filter.getParam())) {
                        //фильтр из родительского маршрута
                        filter.setLink(routeScope.getQueryMapping().get(filter.getParam()));
                    } else if (StringUtils.isJs(prefilterValue)) {
                        String globalDsId = CompileUtil.generateWidgetId(pageScope.getPageId(), preFilter.getDatasource());
                        ReduxModel model = p.cast(preFilter.getModel(), ReduxModel.RESOLVE);
                        ModelLink link = new ModelLink(model, globalDsId);
                        link.setValue(prefilterValue);
                        link.setParam(filter.getParam());
                        filter.setLink(link);
                    } else {
                        //фильтр с константным значением или значением из параметра в url
                        ModelLink link = new ModelLink(prefilterValue);
                        link.setParam(filter.getParam());
                        filter.setLink(link);
                    }
                    filters.add(filter);
                } else {
                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
                }
            }

        }

        return filters;
    }

    private void initMandatoryValidation(N2oDatasource source, CompileProcessor p,
                                         N2oPreFilter preFilter, N2oQuery.Filter queryFilter) {
        if (preFilter.getRequired() != null && preFilter.getRequired()) {
            if (p.getScope(ValidationList.class) != null) {
                MandatoryValidation v = new MandatoryValidation(
                        queryFilter.getFilterField(),
                        p.getMessage("n2o.required.filter"),
                        queryFilter.getFilterField()
                );
                v.setMoment(N2oValidation.ServerMoment.beforeQuery);
                v.setSeverity(SeverityType.danger);

                ValidationList validationList = p.getScope(ValidationList.class);
                validationList.add(source.getId(), ReduxModel.FILTER, v);
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

    private QueryContext getQueryContext(Datasource compiled,
                                         N2oDatasource source,
                                         CompileContext<?, ?> context,
                                         CompileProcessor p,
                                         String route,
                                         List<Filter> filters) {
        QueryContext queryContext = new QueryContext(source.getQueryId(), route, context.getUrlPattern());
        ValidationList validationList = p.getScope(ValidationList.class);
        List<Validation> validations = validationList == null ? null : validationList.get(source.getId(), ReduxModel.FILTER);
//        if (context instanceof PageContext && ((PageContext) context).getSubmitOperationId() != null) {
//            if (object == null)
//                throw new N2oException("submit-operation is defined, but object-id isn't set in widget or query");
//            CompiledObject.Operation operation = object.getOperations().get(((PageContext) context).getSubmitOperationId());
//            if (operation.getValidationList() != null) {
//                if (validations == null) {
//                    validations = operation.getValidationList();
//                } else {
//                    validations.addAll(operation.getValidationList());
//                }
//            }
//        }
        queryContext.setValidations(validations);
        queryContext.setFilters(filters);
        if (source.getDefaultValuesMode() != null)
            queryContext.setUpload(UploadType.values()[source.getDefaultValuesMode().ordinal()]);
//        queryContext.setFailAlertWidgetId(getFailAlertWidget(compiled));fixme
//        queryContext.setSuccessAlertWidgetId(getSuccessAlertWidget(compiled));
//        queryContext.setMessagesForm(getMessagesForm(compiled));
        SubModelsScope subModelsScope = p.getScope(SubModelsScope.class);
        if (subModelsScope != null) {
            queryContext.setSubModelQueries(subModelsScope.get(source.getId()));
        }
        queryContext.setQuerySize(source.getSize());
        CopiedFieldScope copiedFieldScope = p.getScope(CopiedFieldScope.class);
        if (copiedFieldScope != null)
            queryContext.setCopiedFields(copiedFieldScope.getCopiedFields(source.getId()));
        return queryContext;
    }

    private ClientDataProvider initSubmit(N2oDatasource source, Datasource compiled, CompiledObject compiledObject,
                                          CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        N2oClientDataProvider submitProvider = initSubmit(source.getSubmit(), source.getId(), compiledObject, p);

        submitProvider.setSubmitForm(p.cast(source.getSubmit().getSubmitAll(), true));
        submitProvider.setGlobalDatasourceId(compiled.getId());
        submitProvider.setDatasourceId(source.getId());
        submitProvider.getActionContextData().setSuccessAlertWidgetId(source.getSubmit().getMessageWidgetId());
        submitProvider.getActionContextData().setFailAlertWidgetId(source.getSubmit().getMessageWidgetId());
        return compileSubmit(submitProvider, context, p);
    }


    private N2oClientDataProvider initSubmit(Submit submit, String datasourceId, CompiledObject object, CompileProcessor p) {
        if (object == null)
            throw new N2oException(String.format("For compilation submit for datasource [%s] is necessary object!", datasourceId));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setMethod(RequestMethod.POST);
        dataProvider.setUrl(submit.getRoute());
        dataProvider.setTargetModel(ReduxModel.RESOLVE);
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());
        dataProvider.setAutoSubmitOn(submit.getSubmitOn());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(object.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(submit.getRoute());
        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(), false));
        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(), true));
        actionContextData.setMessagePosition(submit.getMessagePosition());
        actionContextData.setMessagePlacement(submit.getMessagePlacement());
        actionContextData.setOperation(object.getOperations().get(submit.getOperationId()));
        if (submit.getRefreshOnSuccess() != null) {
            actionContextData.setRefresh(new RefreshSaga());
            if (submit.getRefreshDatasources() != null)
                actionContextData.getRefresh().setDatasources(Arrays.asList(submit.getRefreshDatasources()));
        }
        dataProvider.setActionContextData(actionContextData);

        return dataProvider;
    }

    private ClientDataProvider compileSubmit(N2oClientDataProvider source, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String path;
        String globalDatasourceId = source.getGlobalDatasourceId();
        ReduxModel targetModel = initTargetWidgetModel(p, source.getTargetModel());

        Map<String, ModelLink> pathMapping = new StrictMap<>();
        pathMapping.putAll(compileParams(source.getPathParams(), context, p, targetModel, globalDatasourceId));
        dataProvider.setFormMapping(compileParams(source.getFormParams(), context, p, targetModel, globalDatasourceId));
        dataProvider.setHeadersMapping(compileParams(source.getHeaderParams(), context, p, targetModel, globalDatasourceId));
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        path = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");
        if (context.getPathRouteMapping() != null)
            pathMapping.putAll(context.getPathRouteMapping());
        path = normalize(path + normalize(p.cast(source.getUrl(), source.getDatasourceId())));
        dataProvider.setPathMapping(pathMapping);
        dataProvider.setMethod(source.getMethod());
        dataProvider.setOptimistic(source.getOptimistic());
        dataProvider.setSubmitForm(source.getSubmitForm());


        ClientDataProviderUtil.initActionContext(source, pathMapping, path, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + p.cast(path, ""));
        dataProvider.setQueryMapping(compileParams(source.getQueryParams(), context, p, targetModel, globalDatasourceId));
        dataProvider.setQuickSearchParam(source.getQuickSearchParam());
        dataProvider.setSize(source.getSize());
        dataProvider.setAutoSubmitOn(source.getAutoSubmitOn());

        return dataProvider;
    }

    private ReduxModel initTargetWidgetModel(CompileProcessor p, ReduxModel defaultModel) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                return modelAware.getModel();
            }
        }
        return defaultModel;
    }

    private Map<String, ModelLink> compileParams(N2oParam[] params, CompileContext<?, ?> context,
                                                 CompileProcessor p, ReduxModel model, String datasourceId) {
        if (params == null)
            return Collections.emptyMap();
        Map<String, ModelLink> result = new StrictMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            if (param.getValueParam() == null) {
                link = getModelLink(p, model, datasourceId, param);
            } else {
                link = getModelLinkByParam(context, param);
            }
            if (link != null)
                result.put(param.getName(), link);
        }
        return result;
    }

    private ModelLink getModelLink(CompileProcessor p, ReduxModel model, String datasourceId, N2oParam param) {
        ModelLink link;
        Object value = param.getValueList() != null ? param.getValueList() :
                ScriptProcessor.resolveExpression(param.getValue());
        if (value == null || StringUtils.isJs(value)) {
            link = new ModelLink(p.cast(param.getModel(), model), datasourceId);
            link.setValue(value);
        } else {
            link = new ModelLink(value);
        }
        return link;
    }

    private ModelLink getModelLinkByParam(CompileContext<?, ?> context, N2oParam param) {
        ModelLink link = null;
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

//    protected String getFailAlertWidget(Widget widget) {fixme
//        return widget.getId();
//    }
//
//    protected String getSuccessAlertWidget(Widget widget) {
//        return widget.getId();
//    }
//
//    protected String getMessagesForm(Widget widget) {
//        return widget.getId();
//    }
}
