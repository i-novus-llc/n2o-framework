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
import net.n2oapp.framework.api.exception.N2oValidationException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MessageSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.register.route.RoutingResult;
import net.n2oapp.framework.api.rest.N2oResponse;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static net.n2oapp.framework.mvc.n2o.N2oServlet.USER;

public abstract class AbstractController {
    private ObjectMapper objectMapper;
    private MetadataRouter router;
    private MetadataEnvironment environment;
    private ErrorMessageBuilder errorMessageBuilder;

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

    public void setErrorMessageBuilder(ErrorMessageBuilder errorMessageBuilder) {
        this.errorMessageBuilder = errorMessageBuilder;
    }

    @SuppressWarnings("unchecked")
    protected ActionRequestInfo createActionRequestInfo(String path, Map<String, String[]> params, Object body, UserContext user) {
        RoutingResult result = router.get(path);
        ActionContext actionCtx = (ActionContext) result.getContext(CompiledObject.class);
        DataSet queryData = actionCtx.getParams(path, params);
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
                    FilterType filterType = typeEntry.getValue();
                    Restriction restriction = new Restriction(fieldId, value, filterType);
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
        QueryContext queryCtx = (QueryContext) result.getContext(CompiledQuery.class);
        DataSet data = queryCtx.getParams(request.getPathInfo(), request.getParameterMap());
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
        QueryContext queryCtx = (QueryContext) result.getContext(CompiledQuery.class);
        DataSet data = queryCtx.getParams(path, params);
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

    protected void handleError(N2oResponse n2oResponse) {
        if (n2oResponse.getErrorInfo() == null) return;
        Exception exception = n2oResponse.getErrorInfo().getException();
        if (exception == null) return;
        MetaSaga meta = new MetaSaga();
        if (exception instanceof N2oValidationException) {
            N2oValidationException e = (N2oValidationException) exception;
            List<ResponseMessage> responseMessages = errorMessageBuilder.buildMessages(e);
            meta.setMessages(new MessageSaga());
            AlertSaga alert = new AlertSaga();
            HashMap<String, ResponseMessage> fields = new HashMap<>();
            List<ResponseMessage> responseMessagesForAlert = new ArrayList<>();
            for (ResponseMessage responseMessage : responseMessages) {
                if (responseMessage.getField() != null)
                    fields.put(responseMessage.getField(), responseMessage);
                else
                    responseMessagesForAlert.add(responseMessage);
            }
            if (!responseMessagesForAlert.isEmpty()) {
                alert.setAlertKey(n2oResponse.getErrorInfo().getAlertKey());
                alert.setMessages(responseMessagesForAlert);
            }
            meta.getMessages().setFields(fields);
            meta.getMessages().setForm(e.getMessageForm());
            if (alert.getMessages() != null || alert.getAlertKey() != null) {
                meta.setAlert(alert);
            }
        } else if (exception instanceof N2oException) {
            ResponseMessage responseMessage = errorMessageBuilder.build(exception);
            meta.setAlert(new AlertSaga());
            meta.getAlert().setMessages(Collections.singletonList(responseMessage));
            meta.getAlert().setAlertKey(n2oResponse.getErrorInfo().getAlertKey());
        }
        n2oResponse.setMeta(meta);
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
}
