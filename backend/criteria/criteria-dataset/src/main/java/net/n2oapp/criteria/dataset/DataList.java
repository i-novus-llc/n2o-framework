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
        c.forEach(e -> add(e instanceof DataSet ? new DataSet((DataSet) e) : e instanceof DataList ? new DataList(((DataList) e)) : e));
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
