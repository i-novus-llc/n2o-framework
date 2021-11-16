package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ValidateType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
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
        compiled.setSize(p.cast(source.getSize(), 10));
        ValidationList validationList = p.getScope(ValidationList.class);
        SubModelsScope subModelsScope = p.getScope(SubModelsScope.class);
        CopiedFieldScope copiedFieldScope = p.getScope(CopiedFieldScope.class);
        PageWidgetsScope widgetsScope = p.getScope(PageWidgetsScope.class);
        CompiledObject object = null;
        if (source.getObjectId() != null)
            object = p.getCompiled(new ObjectContext(source.getObjectId()));
        compiled.setDefaultValuesMode(source.getDefaultValuesMode());
        compiled.setProvider(initDataProvider(compiled, source, context, p, validationList, subModelsScope, copiedFieldScope, widgetsScope, object));
        compiled.setValidations(initValidation(source, widgetsScope));
        compiled.setSubmit(initSubmit(source, object, context, p));
        compiled.setDependencies(initDependencies(source, context, p));
        return compiled;
    }

    private List<DependencyCondition> initDependencies(N2oDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        List<DependencyCondition> fetch = new ArrayList<>();
        String pageId = context.getSourceId((BindProcessor) p);
        if (source.getDependencies() != null) {
            for (N2oDatasource.Dependency d : source.getDependencies()) {
                if (d instanceof N2oDatasource.FetchDependency && ReduxModel.RESOLVE.equals(((N2oDatasource.FetchDependency) d).getModel())) {
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

    private Map<String, List<Validation>> initValidation(N2oDatasource source, PageWidgetsScope widgetsScope) {
        return widgetsScope.getWidgets().values().stream()
                .filter(w -> source.getId().equals(w.getDatasource()) && w instanceof Form)
                .map(w -> ((Form) w).getComponent().getValidation())
                .flatMap(v -> v.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private ClientDataProvider initDataProvider(Datasource compiled, N2oDatasource source, CompileContext<?, ?> context,
                                                CompileProcessor p, ValidationList validationList,
                                                SubModelsScope subModelsScope, CopiedFieldScope copiedFieldScope,
                                                PageWidgetsScope widgetsScope, CompiledObject object) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String url = getDatasourceRoute(compiled, source, p);
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + url);
        initDataProviderMappings(compiled, dataProvider, p);
        SearchBarScope searchBarScope = p.getScope(SearchBarScope.class);
        if (searchBarScope != null && searchBarScope.getWidgetId().equals(source.getId())) {
            PageScope pageScope = p.getScope(PageScope.class);
            String searchWidgetId = pageScope != null ?
                    CompileUtil.generateWidgetId(pageScope.getPageId(), searchBarScope.getWidgetId()) :
                    searchBarScope.getWidgetId();
            ModelLink modelLink = new ModelLink(searchBarScope.getModelPrefix(),
                    pageScope == null || pageScope.getWidgetIdDatasourceMap() == null ?
                            searchWidgetId : pageScope.getWidgetIdDatasourceMap().get(searchWidgetId));
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
        if (source.getQueryId() != null)
            p.addRoute(getQueryContext(compiled, source, context, url, validationList, subModelsScope, copiedFieldScope,
                widgetsScope, object));
        return dataProvider;
    }

    private String getDatasourceRoute(Datasource compiled, N2oDatasource source, CompileProcessor p) {
        String datasource = source.getId();
        String route = normalize(datasource);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
//        if (widgetScope != null && widgetScope.getDependsOnWidgetId() != null && fixme
//                source.getDetailFieldId() != null) {
//            //Если есть master/detail зависимость, то для восстановления необходимо в маршруте добавить идентификатор мастер записи
//            String selectedId = normalizeParam(p.cast(compiled.getMasterParam(), widgetScope.getDependsOnWidgetId() + "_id"));
//            route = normalize(colon(selectedId)) + normalize(datasource);
//        }
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            return RouteUtil.normalize(parentRouteScope.getUrl() + route);
        } else {
            return route;
        }
    }

    private void initDataProviderMappings(Datasource compiled, ClientDataProvider dataProvider, CompileProcessor p) {
        dataProvider.setPathMapping(new StrictMap<>());
        dataProvider.setQueryMapping(new StrictMap<>());
//        if (compiled.getMasterLink() != null) { fixme
//            dataProvider.getPathMapping().put(compiled.getMasterParam(), compiled.getMasterLink());
//        }
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null) {
            dataProvider.getPathMapping().putAll(parentRouteScope.getPathMapping());
            dataProvider.getQueryMapping().putAll(parentRouteScope.getQueryMapping());
        }
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (compiled.getDependencies() != null && compiled.getDependencies() != null) {
            for (DependencyCondition fetch : compiled.getDependencies()) {
                if (fetch.getGlobalMasterWidgetId() != null) {
                    ParentRouteScope masterRouteScope = pageRoutesScope.get(fetch.getGlobalMasterWidgetId());
                    if (masterRouteScope != null) {
                        masterRouteScope.getPathMapping().forEach(parentRouteScope.getPathMapping()::putIfAbsent);
                        masterRouteScope.getQueryMapping().forEach(parentRouteScope.getQueryMapping()::putIfAbsent);
                    }
                }
            }
        }
//        if (compiled.getFilters() != null) {fixme
//            ((List<Filter>) compiled.getFilters()).stream().filter(f -> !dataProvider.getPathMapping().containsKey(f.getParam()))
//                    .forEach(f -> dataProvider.getQueryMapping().put(f.getParam(), f.getLink()));
//        }
    }

    private QueryContext getQueryContext(Datasource compiled, N2oDatasource source, CompileContext<?, ?> context, String route,
                                           ValidationList validationList, SubModelsScope subModelsScope,
                                           CopiedFieldScope copiedFieldScope, PageWidgetsScope widgetsScope, CompiledObject object) {
        QueryContext queryContext = new QueryContext(source.getQueryId(), route, context.getUrlPattern());
        List<Validation> validations = validationList == null ? null : validationList.get(source.getId(), ReduxModel.FILTER);
        if (context instanceof PageContext && ((PageContext) context).getSubmitOperationId() != null) {
            if (object == null)
                throw new N2oException("submit-operation is defined, but object-id isn't set in widget or query");
            CompiledObject.Operation operation = object.getOperations().get(((PageContext) context).getSubmitOperationId());
            if (operation.getValidationList() != null) {
                if (validations == null) {
                    validations = operation.getValidationList();
                } else {
                    validations.addAll(operation.getValidationList());
                }
            }
        }
        List<Filter> filters = new ArrayList<>();
        for (Widget<?> w : widgetsScope.getWidgets().values()) {
            if (source.getId().equals(w.getDatasource())) {
                filters.addAll(w.getFilters());
            }
        }
        queryContext.setValidations(validations);
        queryContext.setFilters(filters);
        if (source.getDefaultValuesMode() != null)
            queryContext.setUpload(UploadType.values()[source.getDefaultValuesMode().ordinal()]);
//        queryContext.setFailAlertWidgetId(getFailAlertWidget(compiled));
//        queryContext.setSuccessAlertWidgetId(getSuccessAlertWidget(compiled));
//        queryContext.setMessagesForm(getMessagesForm(compiled));
        queryContext.setSubModelQueries(subModelsScope.get(source.getId()));
        queryContext.setQuerySize(source.getSize());
        queryContext.setSubModelQueries(subModelsScope.get(source.getId()));
        if (copiedFieldScope != null)
            queryContext.setCopiedFields(copiedFieldScope.getCopiedFields(source.getId()));
        return queryContext;
    }

    private ClientDataProvider initSubmit(N2oDatasource source, CompiledObject compiledObject,
                                                CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        N2oClientDataProvider dataProvider = initFromSubmit(source.getSubmit(), source.getId(), compiledObject, p);

        dataProvider.setSubmitForm(true);
        dataProvider.setDatasourceId(source.getId());

//        dataProvider.getActionContextData().setSuccessAlertWidgetId(source.getId());
//        dataProvider.getActionContextData().setFailAlertWidgetId(source.getId());

        return compileSubmit(dataProvider, context, p);
    }


    private N2oClientDataProvider initFromSubmit(Submit submit, String datasourceId, CompiledObject object, CompileProcessor p) {
        if (object == null)
            throw new N2oException(String.format("For compilation submit for field [%s] is necessary object!", datasourceId));

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
//        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(), false));
//        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(), false));
        actionContextData.setMessagePosition(submit.getMessagePosition());
        actionContextData.setMessagePlacement(submit.getMessagePlacement());
        actionContextData.setOperation(object.getOperations().get(submit.getOperationId()));
        if (submit.getRefreshOnSuccess() != null) {
            actionContextData.setRefresh(new RefreshSaga());
            actionContextData.getRefresh().setType(RefreshSaga.Type.datasource);
            actionContextData.getRefresh().getOptions().setDatasourcesId(Arrays.asList(submit.getRefreshDatasources()));
        }
        dataProvider.setActionContextData(actionContextData);

        return dataProvider;
    }

    private ClientDataProvider compileSubmit(N2oClientDataProvider compiled, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String path = null;
        String datasource = compiled.getDatasourceId() == null ? initSubmitDatasource(context, p) : compiled.getDatasourceId();//fixme
        ReduxModel targetModel = initTargetWidgetModel(p, compiled.getTargetModel());

        if (RequestMethod.POST == compiled.getMethod() ||
                RequestMethod.PUT == compiled.getMethod() ||
                RequestMethod.DELETE == compiled.getMethod()) {
            Map<String, ModelLink> pathMapping = new StrictMap<>();
            pathMapping.putAll(compileParams(compiled.getPathParams(), context, p, targetModel, datasource));
            dataProvider.setFormMapping(compileParams(compiled.getFormParams(), context, p, targetModel, datasource));
            dataProvider.setHeadersMapping(compileParams(compiled.getHeaderParams(), context, p, targetModel, datasource));
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            path = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");
            if (context.getPathRouteMapping() != null)
                pathMapping.putAll(context.getPathRouteMapping());
            path = normalize(path + normalize(p.cast(compiled.getUrl(), compiled.getId(), "")));
            dataProvider.setPathMapping(pathMapping);
            dataProvider.setMethod(compiled.getMethod());
            dataProvider.setOptimistic(compiled.getOptimistic());
            dataProvider.setSubmitForm(compiled.getSubmitForm());

            initActionContext(compiled, pathMapping, p.cast(path, compiled.getUrl()), p);
        }

        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + p.cast(path, compiled.getUrl()));
        dataProvider.setQueryMapping(compileParams(compiled.getQueryParams(), context, p, targetModel, datasource));
        dataProvider.setQuickSearchParam(compiled.getQuickSearchParam());
        dataProvider.setSize(compiled.getSize());
        dataProvider.setAutoSubmitOn(compiled.getAutoSubmitOn());

        return dataProvider;
    }

    private String initSubmitDatasource(CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String targetWidgetId = null;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                targetWidgetId = pageScope == null ?
                        widgetIdAware.getWidgetId() : pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());
            }
        }
        if (targetWidgetId == null) {
            if (widgetScope != null) {
                targetWidgetId = widgetScope.getClientWidgetId();
            } else if (context instanceof PageContext && ((PageContext) context).getResultWidgetId() != null && pageScope != null) {
                targetWidgetId = pageScope.getGlobalWidgetId(((PageContext) context).getResultWidgetId());
            } else {
                throw new N2oException("Unknown widgetId for invoke action!");
            }
        }
        return targetWidgetId;
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
            String widgetId = null;
            if (param.getRefWidgetId() != null) {
                String pageId = param.getRefPageId();
                if (param.getRefPageId() == null) {
                    PageScope pageScope = p.getScope(PageScope.class);
                    if (pageScope != null)
                        pageId = pageScope.getPageId();
                }
                widgetId = CompileUtil.generateWidgetId(pageId, param.getRefWidgetId());
            }
            link = new ModelLink(p.cast(param.getModel(), model), p.cast(widgetId, datasourceId));
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

    private void initActionContext(N2oClientDataProvider source, Map<String, ModelLink> pathMapping,
                                          String url, CompileProcessor p) {
        if (source.getActionContextData() != null) {
            N2oClientDataProvider.ActionContextData actionContextData = source.getActionContextData();
            ActionContext actionContext = new ActionContext(actionContextData.getObjectId(), actionContextData.getOperationId(), url);

            Map<String, ModelLink> routePathMapping = new StrictMap<>();
            Map<String, ModelLink> routeQueryMapping = new StrictMap<>();

            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            if (routeScope != null) {
                routePathMapping.putAll(routeScope.getPathMapping());
                routePathMapping.putAll(pathMapping);
                routeQueryMapping.putAll(routeScope.getQueryMapping());
            }
            actionContext.setPathRouteMapping(routePathMapping);
            actionContext.setQueryRouteMapping(routeQueryMapping);
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope == null || componentScope.unwrap(N2oButton.class) == null
                    || !ValidateType.NONE.equals(componentScope.unwrap(N2oButton.class).getValidate())) {
                ValidationList validationList = p.getScope(ValidationList.class);
                actionContext.setValidations(validationList == null ? null : validationList.get(actionContextData.getFailAlertWidgetId(),
                        initTargetWidgetModel(p, source.getTargetModel())));
            }
            actionContext.setRedirect(actionContextData.getRedirect());
            actionContext.setRefresh(actionContextData.getRefresh());
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null)
                actionContext.setParentPageId(pageScope.getPageId());
            actionContext.setParentWidgetId(actionContextData.getParentWidgetId());
            actionContext.setFailAlertWidgetId(actionContextData.getFailAlertWidgetId());
            actionContext.setMessagesForm(actionContextData.getMessagesForm());
            actionContext.setSuccessAlertWidgetId(actionContextData.getSuccessAlertWidgetId());
            actionContext.setMessageOnSuccess(actionContextData.isMessageOnSuccess());
            actionContext.setMessageOnFail(actionContextData.isMessageOnFail());
            actionContext.setMessagePosition(actionContextData.getMessagePosition());
            actionContext.setMessagePlacement(actionContextData.getMessagePlacement());

            Set<String> formParams = new HashSet<>();
            if (source.getFormParams() != null)
                Arrays.stream(source.getFormParams()).forEach(fp -> formParams.add(fp.getId()));

            Map<String, String> operationMapping = new StrictMap<>();
            for (AbstractParameter inParameter : actionContextData.getOperation().getInParametersMap().values()) {
                if (inParameter instanceof ObjectSimpleField) {
                    String param = ((ObjectSimpleField) inParameter).getParam();
                    // form params from this source should be ignored in operationMapping
                    if (param != null && !formParams.contains(param))
                        operationMapping.put(param, inParameter.getId());
                }
            }
            actionContext.setOperationMapping(operationMapping);
            p.addRoute(actionContext);
        }
    }

//    protected String getFailAlertWidget(Widget widget) {
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
