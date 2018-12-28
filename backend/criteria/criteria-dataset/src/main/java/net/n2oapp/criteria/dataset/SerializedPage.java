package net.n2oapp.criteria.dataset;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.query.QueryPage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 16:59
 */
public class SerializedPage implements Serializable {
    protected Integer count;
    protected List<DataSet> data;

    public SerializedPage(QueryPage queryPage, Map<String, String> fieldsMapping) {
        count = queryPage.getCount();
        CollectionPage<DataSet> dataPage = DataSetConverter.convert(queryPage, fieldsMapping);
        data = (List<DataSet>) dataPage.getCollection();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<DataSet> getData() {
        return data;
    }

    public void setData(List<DataSet> data) {
        this.data = data;
    }
}
