package net.n2oapp.framework.api.script;

import net.n2oapp.criteria.dataset.DataSet;

/**
 * @author rgalina
 * @since 15.02.2016
 */
public class N2oModel {
    private DataSet dataSet;

    public N2oModel(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Object get(String key) {
        return dataSet.get(key);
    }

    public void set(String key, Object o) {
        dataSet.put(key, o);
    }

    public boolean has(String key) {
        return dataSet.containsKey(key);
    }
}
