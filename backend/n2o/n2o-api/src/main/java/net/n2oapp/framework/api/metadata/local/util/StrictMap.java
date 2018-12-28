package net.n2oapp.framework.api.metadata.local.util;

import java.util.LinkedHashMap;

/**
 * User: iryabov
 * Date: 29.10.13
 * Time: 14:38
 */
public class StrictMap<K, V> extends LinkedHashMap<K, V> {
    private String msg = "Value by id = '%s' not found";

    public StrictMap() {
    }

    public StrictMap(String msg) {
        super();
        this.msg = msg;
    }

    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value == null)
            throw new IllegalArgumentException(String.format(msg, key));
        return value;
    }
}
