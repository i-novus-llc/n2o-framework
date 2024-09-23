package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oMandatoryValidation;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
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

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceIds;

/**
 * Утилитный класс для компиляции источников данных
 */
public class DatasourceCompileStaticProcessor {

    public static final String SORTING = "sorting.";

    private DatasourceCompileStaticProcessor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Инициализация запроса за данными
     *
     * @param queryId идентификатор запроса
     * @return скомпилированная модель запроса за данными
     */
    public static CompiledQuery initQuery(String queryId, CompileProcessor p) {
        return queryId != null
                ? p.getCompiled(new QueryContext(queryId))
                : null;
    }

    /**
     * Инициализация объекта
     *
     * @param objectId идентификатор объекта
     * @param queryId  идентификатор запроса за данными
     * @return скомпилированный объект
     */
    public static CompiledObject initObject(String objectId, String queryId, CompileProcessor p) {
        if (objectId != null) {
            return p.getCompiled(new ObjectContext(objectId));
        } else if (queryId != null) {
            return p.getCompiled(new QueryContext(queryId)).getObject();
        }

        return null;
    }

    /**
     * Инициализация провайдера данных клиента для submit
     *
     * @param submit       исходная модель
     * @param datasourceId идентификатор источника данных
     * @param object       объект
     * @return Провайдер данных клиента
     */
    public static N2oClientDataProvider initSubmit(Submit submit, String datasourceId, CompiledObject object, CompileProcessor p) {
        if (object == null) {
            throw new N2oException(String.format("For compilation submit for datasource [%s] is necessary object!", datasourceId));
        }

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setMethod(RequestMethod.POST);
        dataProvider.setUrl(RouteUtil.normalize(submit.getRoute()));
        dataProvider.setTargetModel(ReduxModel.resolve);
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());
        dataProvider.setAutoSubmitOn(submit.getSubmitOn());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(object.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(RouteUtil.normalize(submit.getRoute()));
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
        dataProvider.setSubmitForm(castDefault(submit.getSubmitAll(), true));
        dataProvider.getActionContextData().setMessagesForm(submit.getMessageWidgetId());
        return dataProvider;
    }

    /**
     * Компиляция submit
     *
     * @param source  исходный провайдер данных
     * @param submit  итоговый submit
     * @param context контекст
     */
    public static void compileSubmitClientDataProvider(N2oClientDataProvider source, ClientDataProvider submit, CompileContext<?, ?> context, CompileProcessor p) {
        String clientDatasourceId = source.getClientDatasourceId();
        ReduxModel targetModel = initTargetWidgetModel(p, source.getTargetModel());

        Map<String, ModelLink> pathMapping = new HashMap<>(compileParams(source.getPathParams(), context, targetModel, clientDatasourceId));
        submit.setFormMapping(compileParams(source.getFormParams(), context, targetModel, clientDatasourceId));
        submit.setHeadersMapping(compileParams(source.getHeaderParams(), context, targetModel, clientDatasourceId));
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = castDefault(routeScope != null ? routeScope.getUrl() : null, () -> context.getRoute((N2oCompileProcessor) p), () -> "");
        if (context.getPathRouteMapping() != null) {
            pathMapping.putAll(context.getPathRouteMapping());
        }
        path = normalize(path + normalize(castDefault(source.getUrl(), source.getDatasourceId())));
        submit.setPathMapping(pathMapping);
        submit.setMethod(source.getMethod());
        submit.setOptimistic(source.getOptimistic());
        submit.setSubmitForm(source.getSubmitForm());

        ClientDataProviderUtil.initActionContext(source, pathMapping, path, p);
        submit.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + castDefault(path, ""));
        submit.setQueryMapping(compileParams(source.getQueryParams(), context, targetModel, clientDatasourceId));
        submit.setQuickSearchParam(source.getQuickSearchParam());
        submit.setSize(source.getSize());
        submit.setAutoSubmitOn(source.getAutoSubmitOn());
    }

    /**
     * Компиляция путей
     *
     * @param sourceId   идентификатор исходного datasource
     * @param compiledId идентификатор клиентского datasource
     * @param filters    фильтры
     * @param query      запрос за данными
     */
    public static void compileRoutes(String sourceId, String compiledId, List<Filter> filters, CompiledQuery query,
                                     CompileProcessor p) {
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
            String sortParam = RouteUtil.normalizeParam(SORTING + sourceId + "_" + field.getId());
            BindLink onSet = Redux.createSortLink(compiledId, field.getId());
            ReduxAction onGet = Redux.dispatchSortWidget(compiledId, field.getId(), colon(sortParam));
            routes.addQueryMapping(sortParam, onGet, onSet);
        }
    }

    /**
     * Инициализация пути источника данных
     *
     * @param sourceId    идентификатор исходной модели
     * @param compiledId  идентификатор скомпилированной модели
     * @param sourceRoute исходный путь
     * @return URL источника данных
     */
    public static String getDatasourceRoute(String sourceId, String compiledId, String sourceRoute, CompileProcessor p) {
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        String datasourceRoute = parentRouteScope != null && "/".equals(parentRouteScope.getUrl())
                ? compiledId
                : sourceId;
        String route = castDefault(RouteUtil.normalize(sourceRoute), () -> normalize(datasourceRoute));
        return parentRouteScope != null
                ? RouteUtil.normalize(parentRouteScope.getUrl() + route)
                : route;
    }

    /**
     * Инициализация строки поиска
     *
     * @param datasourceId идентификатор источника данных
     * @param filters      фильтры
     */
    public static void initSearchBar(String datasourceId, List<Filter> filters, CompileProcessor p) {
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

    /**
     * Инициализация скоупа фильтров
     *
     * @param datasourceId идентификатор источника данных
     * @param filters      фильтры
     */
    public static void initFiltersScope(String datasourceId, List<Filter> filters, CompileProcessor p) {
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

    /**
     * Инициализация проверки обязательности поля для фильтра
     *
     * @param datasourceId идентификатор источника данных
     * @param preFilter    фильтр
     * @param queryFilter  фильтр в запросе за данными
     */
    public static void initMandatoryValidation(String datasourceId, N2oPreFilter preFilter, N2oQuery.Filter queryFilter,
                                               CompileContext<?, ?> context, CompileProcessor p) {
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

    /**
     * Инициализация QueryContext
     *
     * @param datasourceCompiledId идентификатор клиентского источника данных
     * @param datasourceId         идентификатор источника данных
     * @param queryId              идентификатор запроса за данными
     * @param size                 количество записей
     * @param route                путь
     * @param filters              фильтры
     * @param query                запрос за данными
     * @return Контекст запроса за данными
     */
    public static QueryContext getQueryContext(String datasourceId,
                                               String datasourceCompiledId,
                                               String queryId,
                                               Integer size,
                                               DefaultValuesMode mode,
                                               CompileContext<?, ?> context,
                                               CompileProcessor p,
                                               String route,
                                               List<Filter> filters,
                                               CompiledQuery query) {
        QueryContext queryContext = new QueryContext(queryId, route, context.getUrlPattern());

        ValidationScope validationScope = p.getScope(ValidationScope.class);
        List<Validation> validations = validationScope == null ? null : validationScope.get(datasourceId, ReduxModel.filter);
        queryContext.setValidations(validations);
        queryContext.setFilters(filters);
        queryContext.setMode(mode);
        queryContext.setMessagesForm(datasourceCompiledId);
        queryContext.setQuerySize(size);
        queryContext.setSortingMap(QueryContextUtil.initSortingMap(query));

        SubModelsScope subModelsScope = p.getScope(SubModelsScope.class);
        if (subModelsScope != null) {
            queryContext.setSubModelQueries(subModelsScope.get(datasourceId));
        }

        CopiedFieldScope copiedFieldScope = p.getScope(CopiedFieldScope.class);
        if (copiedFieldScope != null) {
            queryContext.setCopiedFields(copiedFieldScope.getCopiedFields(datasourceId));
        }

        return queryContext;
    }

    /**
     * Инициализация фильтров
     *
     * @param datasourceId идентификатор источника данных
     * @param preFilters   список исходных фильтров
     * @param query        запрос за данными
     * @return список фильтров
     */
    public static List<Filter> initFilters(String datasourceId, N2oPreFilter[] preFilters, CompileContext<?, ?> context, CompileProcessor p, CompiledQuery query) {
        if (query == null) {
            return null;
        }
        List<Filter> filters = new ArrayList<>();

        if (preFilters != null) {
            for (N2oPreFilter preFilter : preFilters) {
                N2oQuery.Filter queryFilter = query.getFilterByPreFilter(preFilter);

                if (queryFilter == null) {
                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
                }

                Filter filter = new Filter();
                initMandatoryValidation(datasourceId, preFilter, queryFilter, context, p);
                filter.setParam(castDefault(preFilter.getParam(), () -> datasourceId + "_" + queryFilter.getParam()));
                filter.setRoutable(castDefault(preFilter.getRoutable(), false));
                filter.setFilterId(queryFilter.getFilterId());
                filter.setLink(getFilterModelLink(datasourceId, preFilter, filter.getParam(), p));
                if (preFilter.getRequired() != null) {
                    filter.getLink().setRequired(preFilter.getRequired());
                }
                filters.add(filter);
            }
        }
        initSearchBar(datasourceId, filters, p);
        initFiltersScope(datasourceId, filters, p);

        return filters;
    }

    private static ModelLink getFilterModelLink(String datasourceId, N2oPreFilter preFilter, String filterParam, CompileProcessor p) {
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

    private static Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        }

        return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
    }

    private static ReduxModel initTargetWidgetModel(CompileProcessor p, ReduxModel defaultModel) {
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

    private static Map<String, ModelLink> compileParams(N2oParam[] params, CompileContext<?, ?> context,
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

    private static ModelLink getModelLink(ReduxModel model, String clientDatasourceId, N2oParam param) {
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

    private static ModelLink getModelLinkByParam(CompileContext<?, ?> context, N2oParam param) {
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

}
