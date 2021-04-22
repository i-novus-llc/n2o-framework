package net.n2oapp.framework.engine.modules;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.*;

/**
 * Вставка времени выполнения запроса в атрибуты ответа
 * todo нужно задать момент срабатывания самым последним для processAction и processQuery и самым первым для Result и Error (NNO-1438)
 */
public class ResponseTimeProcessing implements DataProcessing {

    private static final String REQUEST_BEGIN_TIME_NAME_DEFAULT = "N2O_DATA_REQUEST_BEGIN_TIME";

    private String requestBeginTimeName;
    private String responseEndTimeName;
    private String responseDeltaTimeName;

    @Override
    public void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        setRequestTime(requestInfo);
    }

    @Override
    public void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        setResponseTime(requestInfo, responseInfo);
    }

    @Override
    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        setResponseTime(requestInfo, responseInfo);
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        setRequestTime(requestInfo);
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        setResponseTime(requestInfo, responseInfo);
    }

    @Override
    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
        setResponseTime(requestInfo, responseInfo);
    }

    private void setRequestTime(RequestInfo requestInfo) {
        requestInfo.addAttribute(getBeginTimeAttributeName(), System.currentTimeMillis());
    }

    private String getBeginTimeAttributeName() {
        return requestBeginTimeName != null ? requestBeginTimeName : REQUEST_BEGIN_TIME_NAME_DEFAULT;
    }

    private void setResponseTime(RequestInfo requestInfo, ResponseInfo responseInfo) {
        Object beginTime = requestInfo.getAttribute(getBeginTimeAttributeName());
        if (beginTime != null) {
            long endTime = System.currentTimeMillis();
            if (requestBeginTimeName != null)
                responseInfo.addAttribute(requestBeginTimeName, beginTime);
            if (responseEndTimeName != null)
                responseInfo.addAttribute(responseEndTimeName, endTime);
            if (responseDeltaTimeName != null)
                responseInfo.addAttribute(responseDeltaTimeName, endTime - (long) beginTime);
        }
    }

    public void setRequestBeginTimeName(String requestBeginTimeName) {
        this.requestBeginTimeName = requestBeginTimeName;
    }

    public void setResponseDeltaTimeName(String responseDeltaTimeName) {
        this.responseDeltaTimeName = responseDeltaTimeName;
    }

    public void setResponseEndTimeName(String responseEndTimeName) {
        this.responseEndTimeName = responseEndTimeName;
    }
}
