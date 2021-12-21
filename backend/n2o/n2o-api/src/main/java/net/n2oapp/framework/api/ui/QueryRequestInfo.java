package net.n2oapp.framework.api.ui;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

/**
 * Информация о запросе данных выборки
 */
public class QueryRequestInfo extends RequestInfo {

    private CompiledQuery query;
    private N2oPreparedCriteria criteria;
    private DataSet data;
    private UploadType upload;
    private int size;

    public CompiledQuery getQuery() {
        return query;
    }

    public N2oPreparedCriteria getCriteria() {
        return criteria;
    }


    public int getSize() {
        return size;
    }

    public DataSet getData() {
        return data;
    }

    public void setQuery(CompiledQuery query) {
        this.query = query;
    }

    public void setCriteria(N2oPreparedCriteria criteria) {
        this.criteria = criteria;
    }

    public void setData(DataSet data) {
        this.data = data;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public UploadType getUpload() {
        return upload;
    }

    public void setUpload(UploadType upload) {
        this.upload = upload;
    }

    public QueryRequestInfo copy() {
        QueryRequestInfo queryRequestInfo = new QueryRequestInfo();
        queryRequestInfo.setUser(getUser());
        if (getData() != null)
            queryRequestInfo.setData(new DataSet(getData()));
        queryRequestInfo.setUpload(getUpload());
        queryRequestInfo.setSize(getSize());
        queryRequestInfo.setQuery(getQuery());
        queryRequestInfo.setCriteria(getCriteria());
        return queryRequestInfo;
    }

    public boolean isValidationEnable() {
        return getQuery() != null && getQuery().getValidations() != null && !getQuery().getValidations().isEmpty();
    }

    public boolean isSubModelsExists() {
        return getQuery() != null && getQuery().getSubModelQueries() != null && !getQuery().getSubModelQueries().isEmpty();
    }
}
