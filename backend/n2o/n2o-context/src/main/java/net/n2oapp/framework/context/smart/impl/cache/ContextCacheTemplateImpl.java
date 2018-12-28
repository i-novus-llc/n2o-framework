package net.n2oapp.framework.context.smart.impl.cache;

import net.n2oapp.cache.template.CacheCallback;
import net.n2oapp.cache.template.CacheTemplate;
import net.n2oapp.framework.context.smart.impl.ProxyContext;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Кэш контекста
 */
public class ContextCacheTemplateImpl extends CacheTemplate implements ContextCacheTemplate {

    public static final String CACHE_NAME = "n2o.context";

    public ContextCacheTemplateImpl(CacheManager cacheManager) {
        setCacheManager(cacheManager);
    }


    @Override
    @SuppressWarnings("unchecked")
    public Object execute(String param, Set<String> dependsOnParams, ProxyContext prevProxyContext, CacheCallback callback) {
        if (prevProxyContext.isCachedParam(param)) {
            return super.execute(CACHE_NAME, getKey(param, dependsOnParams, prevProxyContext), callback);
        } else {
            return callback.doInCacheMiss();
        }
    }


    public Object getKey(String param, Set<String> dependsOnParam, ProxyContext prevProxyContext) {
//        ProxyContext newProxyContext = new ProxyContext(prevProxyContext.getContextEngine(), prevProxyContext.getBaseParams());
        Map<String, Object> parents = new HashMap<>();
        if (dependsOnParam != null) {
            for (String parent : dependsOnParam) {
                parents.put(parent, prevProxyContext.get(parent));
            }
        }
        return new CacheKey(param, parents);
    }

    @Override
    public void putToCache(Map<String, Object> dataSet, Set<String> dependsOnParam, ProxyContext prevProxyContext) {
        Cache cache = getCacheManager().getCache(CACHE_NAME);
        if (cache != null) {
            for (String param : dataSet.keySet()) {
                cache.put(getKey(param, dependsOnParam, prevProxyContext), dataSet.get(param));
            }
        }
    }

    @Override
    public void evictCache() {
        Cache cache = getCacheManager().getCache(CACHE_NAME);
        cache.clear();
    }

    public static class CacheKey implements Serializable {
        private Map<String, Object> parentValues;
        private String var;

        public CacheKey(String var, Map<String, Object> parentValues) {
            this.parentValues = parentValues;
            this.var = var;
        }

        public String getVar() {
            return var;
        }

        public Map<String, Object> getParentValues() {
            return parentValues;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof CacheKey))
                return false;

            CacheKey cacheKey = (CacheKey) o;

            if (parentValues != null ? !parentValues.equals(cacheKey.parentValues) : cacheKey.parentValues != null)
                return false;
            if (!var.equals(cacheKey.var))
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = parentValues != null ? parentValues.hashCode() : 0;
            result = 31 * result + var.hashCode();
            return result;
        }
    }
}
