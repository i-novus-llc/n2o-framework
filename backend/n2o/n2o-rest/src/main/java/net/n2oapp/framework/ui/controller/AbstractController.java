package net.n2oapp.framework.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.api.Direction;
import net.n2oapp.criteria.api.Sorting;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.register.route.RoutingResult;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static net.n2oapp.framework.mvc.n2o.N2oServlet.USER;

public abstract class AbstractController {
    private ObjectMapper objectMapper;
    private MetadataRouter router;
    private MetadataEnvironment environment;

    public AbstractController() {
    }

    public AbstractController(ObjectMapper objectMapper, MetadataRouter router, ReadCompileBindTerminalPipeline pipeline, DomainProcessor domainProcessor) {
        this.objectMapper = objectMapper;
        this.router = router;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setRouter(MetadataRouter router) {
        this.router = router;
    }

    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
    }

    @SuppressWarnings("unchecked")
    protected ActionRequestInfo createActionRequestInfo(String path, Map<String, String[]> params, Object body, UserContext user) {
        RoutingResult result = router.get(path);
        DataSet queryData = getQueryData(params, result);
        ActionContext actionCtx = (ActionContext) result.getContext(CompiledObject.class);
        CompiledObject object = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(actionCtx, queryData);
        CompiledObject.Operation operation = object.getOperations().get(actionCtx.getOperationId());
        ActionRequestInfo<DataSet> requestInfo = new ActionRequestInfo();
        requestInfo.setQueryData(queryData);
        requestInfo.setUser(user);
        requestInfo.setObject(object);
        requestInfo.setOperation(operation);
        requestInfo.setData((DataSet) body);
        requestInfo.setRedirect(actionCtx.getRedirect());
        requestInfo.setSuccessAlertWidgetId(actionCtx.getSuccessAlertWidgetId());
        requestInfo.setFailAlertWidgetId(actionCtx.getFailAlertWidgetId());
        requestInfo.setMessagesForm(actionCtx.getMessagesForm());
        //requestInfo.setChoice(); todo
        return requestInfo;
    }

    private void prepareSelectedId(QueryRequestInfo requestInfo) {
        String selectedId = requestInfo.getData().getString("selectedId");
        if (requestInfo.getQuery() == null) return;
        if (selectedId == null) return;
        N2oQuery.Field fieldPK = requestInfo.getQuery().getFieldsMap().get(N2oQuery.Field.PK);
        String domain = fieldPK != null ? fieldPK.getDomain() : null;
        requestInfo.setSelectedId(environment.getDomainProcessor().doDomainConversion(domain, selectedId));
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
        if (query != null) {
            criteria.setSortings(getSortings(data, queryCtx.getSortingMap()));
            prepareRestrictions(query, criteria, data);
        }
        return criteria;
    }


    private List<Sorting> getSortings(DataSet data, Map<String, String> sortingMap) {
        List<Sorting> sortings = new ArrayList<>();
        DataSet sortingsData = data.getDataSet("sorting");
        if (sortingsData == null)
            return sortings;
        for (String key : sortingsData.flatKeySet()) {
            String fieldId = sortingMap == null || !sortingMap.containsKey(key) ? key : sortingMap.get(key);
            String value = sortingsData.getString(key);
            Direction direction = value != null ? Direction.valueOf(value.toUpperCase()) : Direction.ASC;
            sortings.add(new Sorting(fieldId, direction));
        }
        return sortings;
    }

    private void prepareRestrictions(CompiledQuery query, N2oPreparedCriteria criteria, DataSet data) {
        for (Map.Entry<String, String> paramEntry : query.getParamToFilterIdMap().entrySet()) {
            Object value = data.get(paramEntry.getKey());
            if (value != null) {
                String filterId = paramEntry.getValue();
                if (query.getInvertFiltersMap().containsKey(filterId)) {
                    Map.Entry<String, FilterType> typeEntry = query.getInvertFiltersMap().get(filterId);
                    String fieldId = typeEntry.getKey();
                    N2oQuery.Field field = query.getFieldsMap().get(fieldId);
                    FilterType filterType = typeEntry.getValue();
                    Restriction restriction = new Restriction(fieldId, environment.getDomainProcessor().doDomainConversion(field.getDomain(), value), filterType);
                    criteria.addRestriction(restriction);
                } else {
                    criteria.putAdditionalField(filterId, value);
                }
            }
        }
    }

    @Deprecated
    protected QueryRequestInfo createQueryRequestInfo(HttpServletRequest request) {
        CompiledQuery query;
        RoutingResult result = getRoutingResult(request);
        DataSet data = getQueryData(request, result);
        QueryContext queryCtx = (QueryContext) result.getContext(CompiledQuery.class);
        query = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(queryCtx, data);
        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setUser(getUser(request));
        requestInfo.setQuery(query);
        requestInfo.setData(data);
        requestInfo.setUpload(queryCtx.getUpload() != null ? queryCtx.getUpload() : UploadType.query);
        requestInfo.setCriteria(prepareCriteria(requestInfo.getQuery(), data, queryCtx));
        requestInfo.setSuccessAlertWidgetId(queryCtx.getSuccessAlertWidgetId());
        requestInfo.setFailAlertWidgetId(queryCtx.getFailAlertWidgetId());
        requestInfo.setMessagesForm(queryCtx.getMessagesForm());
        request.setAttribute("messageForm", queryCtx.getFailAlertWidgetId());
        prepareSelectedId(requestInfo);
        return requestInfo;
    }

    protected QueryRequestInfo createQueryRequestInfo(String path, Map<String, String[]> params, UserContext user) {
        CompiledQuery query;
        RoutingResult result = router.get(path);
        DataSet data = getQueryData(params, result);
        QueryContext queryCtx = (QueryContext) result.getContext(CompiledQuery.class);
        query = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(queryCtx, data);
        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setUser(user);
        requestInfo.setQuery(query);
        requestInfo.setData(data);
        requestInfo.setUpload(queryCtx.getUpload() != null ? queryCtx.getUpload() : UploadType.query);
        requestInfo.setCriteria(prepareCriteria(requestInfo.getQuery(), data, queryCtx));
        requestInfo.setSuccessAlertWidgetId(queryCtx.getSuccessAlertWidgetId());
        requestInfo.setFailAlertWidgetId(queryCtx.getFailAlertWidgetId());
        requestInfo.setMessagesForm(queryCtx.getMessagesForm());
        requestInfo.setSize(requestInfo.getCriteria().getSize());
        prepareSelectedId(requestInfo);
        return requestInfo;
    }

    private Object getBody(HttpServletRequest request) {
        try {
            if (request.getReader() == null) return new DataSet();
            String body = IOUtils.toString(request.getReader()).trim();
            if (body.startsWith("[")) {
                return objectMapper.<List<DataSet>>readValue(body,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DataSet.class)
                );
            } else {
                return objectMapper.readValue(body, DataSet.class);
            }
        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

    private UserContext getUser(HttpServletRequest req) {
        UserContext user = (UserContext) req.getAttribute(USER);
        if (user == null)
            throw new IllegalStateException("User is not initialized");
        return user;
    }

    private <D extends Compiled> RoutingResult getRoutingResult(HttpServletRequest req) {
        String path = req.getPathInfo();
        return router.get(path);
    }

    private <D extends Compiled> RoutingResult getRoutingResult(String url) {
        return router.get(url);
    }

    private <D extends Compiled> DataSet getQueryData(HttpServletRequest req, RoutingResult routingResult) {
        DataSet data = new DataSet();
        data.putAll(routingResult.getParams());
        req.getParameterMap().forEach((k, v) -> {
            if (v.length == 1) {
                data.put(k, v[0]);
            } else {
                data.put(k, Arrays.asList(v));
            }
        });
        return data;
    }

    private <D extends Compiled> DataSet getQueryData(Map<String, String[]> params, RoutingResult routingResult) {
        DataSet data = new DataSet();
        data.putAll(routingResult.getParams());
        params.forEach((k, v) -> {
            if (v.length == 1) {
                data.put(k, v[0]);
            } else {
                data.put(k, Arrays.asList(v));
            }
        });
        return data;
    }
}
