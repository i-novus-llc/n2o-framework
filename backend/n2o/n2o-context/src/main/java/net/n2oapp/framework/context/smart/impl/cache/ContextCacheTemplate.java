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
public interface ContextCacheTemplate {

    public Object execute(String param, Set<String> dependsOnParams, ProxyContext prevProxyContext, CacheCallback callback);

    /**
    * добавляет значения из {@code dataSet} в кэш контекста
    *
    * @param dataSet значения, которые необходимо добавить в кэш
    * @param prevProxyContext прокси контекст, используемый для получения ключей для кэша
    */
    public void putToCache(Map<String, Object> dataSet, Set<String> dependsOnParams, ProxyContext prevProxyContext);

    public void evictCache();


}
