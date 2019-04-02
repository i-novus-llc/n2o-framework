package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

import java.util.Collections;
import java.util.List;

/**
 * Ответ на запрос получения данных
 */
@Getter
@Setter
public class GetDataResponse extends N2oResponse {
    @JsonProperty
    private List<DataSet> list;
    @JsonProperty
    private Integer count;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private Integer page;

    public GetDataResponse() {
    }

    public GetDataResponse(DataSet dataSet, Criteria criteria, String messagesForm, QueryResponseInfo responseInfo) {
        list = Collections.singletonList(dataSet);
        size = criteria.getSize();
        count = 1;
        page = criteria.getPage();
        responseInfo.prepare(list);
        setMessagesForm(messagesForm);
        setResponseMessages(responseInfo.getMessageList(), responseInfo.getStackedMessages());
    }

    public GetDataResponse(CollectionPage<DataSet> collectionPage, String messagesForm, QueryResponseInfo responseInfo) {
        list = (List<DataSet>) collectionPage.getCollection();
        size = collectionPage.getCriteria().getSize();
        count = collectionPage.getCount();
        page = collectionPage.getCriteria().getPage();
        responseInfo.prepare(list);
        setMessagesForm(messagesForm);
        setResponseMessages(responseInfo.getMessageList(), responseInfo.getStackedMessages());
    }
}
