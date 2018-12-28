package net.n2oapp.framework.context.smart.impl.cache;

import net.n2oapp.cache.template.CacheCallback;
import net.n2oapp.framework.context.smart.impl.ProxyContext;

import java.util.Map;
import java.util.Set;

/**
 * User: operhod
 * Date: 19.05.14
 * Time: 11:42
 */
public class DummyContextCacheTemplate implements ContextCacheTemplate {

    @Override
    public Object execute(String param, Set<String> dependsOnParams, ProxyContext prevProxyContext, CacheCallback callback) {
        return callback.doInCacheMiss();
    }

    @Override
    public void putToCache(Map<String, Object> dataSet, Set<String> dependsOnParams, ProxyContext prevProxyContext) {

    }

    @Override
    public void evictCache() {

    }


}
