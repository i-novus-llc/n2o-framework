package net.n2oapp.cache.template;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 12.12.13
 * Time: 15:44
 */
public class CacheKey implements Serializable {
    public CacheKey(int operationId, String... keys) {
        this.keys = keys;
        this.operationId = operationId;
    }

    private String[] keys;
    private int operationId;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (String key : keys)
            result = prime * result + key.hashCode();
        result = prime * result + operationId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CacheKey other = (CacheKey) obj;
        for (int i = 0; i < keys.length; i++)
            if (!keys[i].equals(other.keys[i]))
                return false;
        return true;
    }

    public String getFirstKey() {
        return this.keys[0];
    }

    public String getSecondKey() {
        return this.keys[1];
    }

}
