package net.n2oapp.criteria.dataset;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Список данных объекта
 */
public class DataList extends NestedList {
    public DataList() {
    }

    public DataList(Collection<?> c) {
        super();
        c.forEach(e -> {
            if (e instanceof DataSet dataSet) {
                add(new DataSet(dataSet));
            } else if (e instanceof DataList dataList) {
                add(new DataList(dataList));
            } else {
                add(e);
            }
        });
    }

    @Override
    protected NestedMap createNestedMap(Map map) {
        if (map != null)
            return new DataSet(map);
        else
            return new DataSet();
    }

    @Override
    protected NestedList createNestedList(List list) {
        if (list != null)
            return new DataList(list);
        else
            return new DataList();
    }
}
