package net.n2oapp.criteria.dataset;

import net.n2oapp.criteria.api.CollectionPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 17:16
 */
public class DataSetConverter {
    public static CollectionPage<DataSet> convert(CollectionPage collectionPage, Map<String, String> fieldsMapping) {
        Collection collection = collectionPage.getCollection();
        List<DataSet> dataSetPage = convert(collection, fieldsMapping);
        return new CollectionPage<>(collectionPage.getCount(), dataSetPage, collectionPage.getCriteria());
    }

    public static List<DataSet> convert(Collection collection, Map<String, String> fieldsMapping) {
        List<DataSet> dataSetPage = new ArrayList<>(collection.size());
        for (Object row : collection) {
            dataSetPage.add(DataSetUtil.extract(row, fieldsMapping));
        }
        return dataSetPage;
    }
}
