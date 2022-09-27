package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.Collections;
import java.util.List;

/**
 * Ответ на запрос получения данных
 */
@Getter
@Setter
public class GetDataResponse extends N2oResponse {
    @JsonProperty
    @JsonInclude
    private List<DataSet> list;
    @JsonProperty
    private Integer count;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private Integer page;

    public GetDataResponse() {
    }

    public GetDataResponse(List<ResponseMessage> messages, String widgetId) {
        super(messages, widgetId);
    }

    public GetDataResponse(DataSet dataSet, Criteria criteria, QueryResponseInfo responseInfo, String widgetId) {
        list = Collections.singletonList(dataSet);
        size = criteria.getSize();
        count = 1;
        page = criteria.getPage();
        setResponseMessages(responseInfo.getMessageList(), widgetId);
    }

    public GetDataResponse(CollectionPage<DataSet> collectionPage, QueryResponseInfo responseInfo, String widgetId) {
        list = (List<DataSet>) collectionPage.getCollection();
        size = collectionPage.getCriteria().getSize();
        count = collectionPage.getCount();
        page = collectionPage.getCriteria().getPage();
        setResponseMessages(responseInfo.getMessageList(), widgetId);
    }
}
