package net.n2oapp.framework.ui.controller;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.Sorting;
import net.n2oapp.criteria.api.SortingDirectionEnum;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.ValidationRequestInfo;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.register.route.N2oRouter;

import java.util.*;

@Setter
public abstract class AbstractController {
    private MetadataRouter router;
    private MetadataEnvironment environment;
    @Getter
    private AlertMessageBuilder messageBuilder;

    protected AbstractController(MetadataEnvironment environment) {
        this.environment = environment;
        this.router = new N2oRouter(environment, environment.getReadCompilePipelineFunction().apply(new N2oPipelineSupport(environment)));
    }

    protected AbstractController(MetadataEnvironment environment, MetadataRouter router) {
        this.environment = environment;
        this.router = router;
    }

    @SuppressWarnings("unchecked")
    protected ActionRequestInfo createActionRequestInfo(String path,
                                                        Map<String, String[]> queryParams,
                                                        Map<String, String[]> headerParams,
                                                        Object body,
                                                        UserContext user) {
        ActionContext actionCtx = (ActionContext) router.get(path, CompiledObject.class, queryParams);
        DataSet queryData = actionCtx.getParams(path, queryParams);
        CompiledObject object = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(actionCtx, queryData);
        CompiledObject.Operation operation = object.getOperations().get(actionCtx.getOperationId());

        DataSet bodyData = convertToDataSet(body);
        putParams(headerParams, bodyData, actionCtx.getOperationMapping());
        putParams(queryParams, bodyData, actionCtx.getOperationMapping());
        putParams(queryData, bodyData, actionCtx.getOperationMapping());

        ActionRequestInfo<DataSet> requestInfo = new ActionRequestInfo<>();
        requestInfo.setContext(actionCtx);
        requestInfo.setQueryData(queryData);
        requestInfo.setData(bodyData);
        requestInfo.setUser(user);
        requestInfo.setObject(object);
        requestInfo.setOperation(operation);
        requestInfo.setClearDatasource(actionCtx.getClearDatasource());
        requestInfo.setPollingEndCondition(actionCtx.getPollingEndCondition());
        requestInfo.setPolling(actionCtx.getPolling());
        requestInfo.setLoading(actionCtx.getLoading());
        requestInfo.setRedirect(actionCtx.getRedirect());
        requestInfo.setRefresh(actionCtx.getRefresh());
        requestInfo.setMessageOnSuccess(actionCtx.isMessageOnSuccess());
        requestInfo.setMessageOnFail(actionCtx.isMessageOnFail());
        requestInfo.setUseFailOut(actionCtx.isUseFailOut());
        requestInfo.setMessagePosition(actionCtx.getMessagePosition());
        requestInfo.setMessagePlacement(actionCtx.getMessagePlacement());
        requestInfo.setMessagesForm(actionCtx.getMessagesForm());
        return requestInfo;
    }

    protected ValidationRequestInfo createValidationRequestInfo(String path,
                                                                Object body) {
        PageContext pageContext = (PageContext) router.get(path, Page.class, null);
        DataSet queryData = pageContext.getParams(path, null);
        Page page = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(pageContext, queryData);
        DataSet data = convertToDataSet(body);
        ValidationRequestInfo requestInfo = new ValidationRequestInfo();
        String datasourceId = (String) data.get("datasourceId");
        String validationId = (String) data.get("validationId");
        if (datasourceId == null || validationId == null)
            throw new IllegalArgumentException("For validation you should set datasourceId and validationId");
        if (page.getDatasources() == null || page.getDatasources().get(datasourceId) == null)
            throw new IllegalArgumentException(String.format("Datasource by id=%s not found", datasourceId));
        requestInfo.setValidation(getValidationById(page.getDatasources().get(datasourceId).getValidations(), validationId));
        requestInfo.setData(data.getDataSet("data"));
        return requestInfo;
    }

    private void putParams(Map<String, String[]> params, DataSet data, Map<String, String> mapping) {
        if (params != null && mapping != null) {
            for (Map.Entry<String, String> entry : mapping.entrySet()) {
                String[] value = params.get(entry.getKey());
                if (value != null) {
                    if (value.length == 1) {
                        data.put(entry.getValue(), value[0]);
                    } else {
                        data.put(entry.getValue(), Arrays.asList(value));
                    }
                }
            }
        }
    }

    private void putParams(DataSet params, DataSet data, Map<String, String> mapping) {
        if (params != null && mapping != null) {
            for (Map.Entry<String, String> entry : mapping.entrySet()) {
                Object value = params.get(entry.getKey());
                if (value != null)
                    data.put(entry.getValue(), value);
            }
        }
    }

    private DataSet convertToDataSet(Object body) {
        if (body instanceof DataSet dataSet)
            return dataSet;
        else if (body instanceof List bodyList) {
            DataSet dataSet = new DataSet("$list", body);
            dataSet.put("$count", bodyList.size());
            return dataSet;
        }
        return new DataSet((Map<? extends String, ?>) body);
    }

    private N2oPreparedCriteria prepareCriteria(CompiledQuery query, DataSet data, QueryContext queryCtx) {
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        Integer page = data.getInteger("page");
        criteria.setPage(page != null ? page : 1);
        Integer size = data.getInteger("size");
        criteria.setSize(
                size != null ? size : (queryCtx.getQuerySize() != null ? queryCtx.getQuerySize() : 10)
        );
        Integer count = data.getInteger("count");
        criteria.setCount(count);
        Boolean withCount = data.getBoolean("withCount");
        criteria.setWithCount(withCount);
        if (query != null) {
            criteria.setSortings(getSortings(data, queryCtx.getSortingMap()));
            prepareRestrictions(query, criteria, queryCtx, data);
        }

        return criteria;
    }


    private List<Sorting> getSortings(DataSet data, Map<String, String> sortingMap) {
        List<Sorting> sortings = new ArrayList<>();
        if (sortingMap == null)
            return sortings;
        for (String key : sortingMap.keySet()) {
            String fieldId = sortingMap.get(key);
            String value = data.getString(key);
            if (value != null) {
                SortingDirectionEnum direction = SortingDirectionEnum.valueOf(value.toUpperCase());
                sortings.add(new Sorting(fieldId, direction));
            }
        }
        return sortings;
    }

    private void prepareRestrictions(CompiledQuery query, N2oPreparedCriteria criteria, QueryContext queryCtx, DataSet data) {
        if (queryCtx.getFilters() != null)
            for (Filter filter : queryCtx.getFilters()) {
                String key = filter.getParam() == null ? filter.getFilterId() : filter.getParam();
                Object value = data.get(key);
                if (value != null) {
                    String filterId = query.getParamToFilterIdMap().get(key);
                    createFilter(query, criteria, value, filterId);
                }
            }
    }

    private void createFilter(CompiledQuery query, N2oPreparedCriteria criteria, Object value, String filterId) {
        if (query.getInvertFiltersMap().containsKey(filterId)) {
            Map.Entry<String, FilterTypeEnum> typeEntry = query.getInvertFiltersMap().get(filterId);
            String fieldId = typeEntry.getKey();
            FilterTypeEnum filterType = typeEntry.getValue();
            Restriction restriction = new Restriction(fieldId, value, filterType);
            criteria.addRestriction(restriction);
        } else {
            criteria.putAdditionalField(filterId, value);
        }
    }

    protected QueryRequestInfo createQueryRequestInfo(String path, Map<String, String[]> params, UserContext user) {
        CompiledQuery query;
        QueryContext queryCtx = (QueryContext) router.get(path, CompiledQuery.class, params);
        DataSet data = queryCtx.getParams(path, params);
        query = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(queryCtx, data);
        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setUser(user);
        requestInfo.setQuery(query);
        requestInfo.setData(data);
        requestInfo.setMode(queryCtx.getMode() != null ? queryCtx.getMode() : DefaultValuesModeEnum.query);
        requestInfo.setCriteria(prepareCriteria(requestInfo.getQuery(), data, queryCtx));
        requestInfo.setMessagesForm(queryCtx.getMessagesForm());
        requestInfo.setSize(requestInfo.getCriteria().getSize());
        return requestInfo;
    }

    private Validation getValidationById(Map<String, List<Validation>> validations, String validationId) {
        Optional<Validation> validation = validations.values().stream().flatMap(List::stream)
                .filter(v -> v.getId().equals(validationId)).findFirst();
        return validation.orElse(null);
    }
}
