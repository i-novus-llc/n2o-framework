package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * User: operehod
 * Date: 26.11.2014
 * Time: 19:10
 */
public abstract class QueryUnit implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(QueryUnit.class);

    //source model
    private String queryId;
    private N2oPreFilter[] preFilters;

    //вспомогательные штуки, полученные из query
    protected Map<String, Map<FilterType, N2oQuery.Filter>> filtersMap;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }


    public N2oPreFilter[] getPreFilters() {
        return preFilters;
    }

    public void setPreFilters(N2oPreFilter[] preFilters) {
        this.preFilters = preFilters;
    }

}
