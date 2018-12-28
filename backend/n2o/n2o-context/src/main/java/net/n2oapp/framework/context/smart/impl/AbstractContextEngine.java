package net.n2oapp.framework.context.smart.impl;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.context.smart.impl.api.ContextProvider;
import net.n2oapp.framework.context.smart.impl.api.PersistentContextProvider;
import net.n2oapp.framework.context.smart.impl.cache.ContextCacheTemplate;
import net.n2oapp.framework.context.smart.impl.cache.DummyContextCacheTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 16:18
 */
public abstract class AbstractContextEngine implements ApplicationContextAware, ContextEngine {

    protected ApplicationContext applicationContext;
    protected volatile Map<String, ContextProvider> providerMap;
    protected volatile Map<String, PersistentContextProvider> persistentProviderMap;
    protected ContextCacheTemplate cacheTemplate;

    public AbstractContextEngine(ContextCacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    public AbstractContextEngine() {
        this.cacheTemplate = new DummyContextCacheTemplate();
    }


    @Override
    public Object get(String param, Map<String, Object> baseParams) {
        if (baseParams.containsKey(param))
            return baseParams.get(param);
        return new ProxyContext(this, new HashMap<>(baseParams)).get(param);
    }


    @SuppressWarnings("unchecked")
    protected Object get(final String param, final ProxyContext prevProxyContext) {
        final ContextProvider contextProvider = findProvider(param);
        if (prevProxyContext.isBaseParam(param))
            return actualGet(param, contextProvider, prevProxyContext);
        return cacheTemplate.execute(param, contextProvider.getDependsOnParams(), prevProxyContext,
                () -> actualGet(param, contextProvider, prevProxyContext));
    }

    private Object actualGet(final String param, final ContextProvider contextProvider, final ProxyContext prevProxyContext) {
        if (contextProvider == null) {
            throw new ContextException(String.format("provider for param '%s' is not found", param));
        }
        ProxyContext newProxyContext = new ProxyContext(prevProxyContext, contextProvider.getParams(), contextProvider.getDependsOnParams());
        return contextProvider.get(newProxyContext).get(param);
    }


    @Override
    public void set(Map<String, Object> dataSet, Map<String, Object> baseParams) {
        baseParams = new HashMap<>(baseParams);
        baseParams.putAll(dataSet);
        PersistentProxyContext proxyContext = new PersistentProxyContext(this, baseParams);
        proxyContext.set(dataSet);
    }


    /**
    * set значений из dataset в контекст
    * @return мапу объектов, для которых нашелся PersistentContextProvider и отработал метод set
    * */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> set(Map<String, Object> dataSet, ProxyContext prevProxyContext) {
        Map<String, Object> copiedDataSet = new HashMap<>(dataSet);
        if (persistentProviderMap == null)
            initPersistentProviders();
        setValues(copiedDataSet, prevProxyContext);
        Map<String, Object> result = new HashMap<>(dataSet);
        for (String key : copiedDataSet.keySet()) {
            result.remove(key);
        }
        return result;
    }

    private void setValues(Map<String, Object> dataSet, ProxyContext prevProxyContext) {
        PersistentContextProvider provider = findSomeProvider(dataSet);
        if (provider != null) {
            Map<String, Object> expectedValues = new HashMap<>();
            for (String expectedKey : provider.getParams()) {
                if (!dataSet.containsKey(expectedKey))
                    throw new ContextException(String.format("not found expected key (or key is null) '%s' for persist (provider:'%s').", expectedKey, provider.getClass().getName()));
                expectedValues.put(expectedKey, dataSet.get(expectedKey));
                dataSet.remove(expectedKey);
            }
            PersistentProxyContext newProxyContext = new PersistentProxyContext(prevProxyContext, provider.getParams(), provider.getDependsOnParams());
            provider.set(newProxyContext, expectedValues);
            cacheTemplate.putToCache(expectedValues, provider.getDependsOnParams(), newProxyContext);
            setValues(dataSet, prevProxyContext);
        }
    }

    private PersistentContextProvider findSomeProvider(Map<String, Object> dataSet) {
        PersistentContextProvider provider = null;
        for (String key : dataSet.keySet()) {
            provider = persistentProviderMap.get(key);
            if (provider != null)
                break;
        }
        return provider;
    }

    protected ContextProvider findProvider(String param) {
        if (providerMap == null)
            initProviders();
        ContextProvider provider = providerMap.get(param);
        if (provider == null) {
            throw new ContextException(String.format("provider for param '%s' is not found", param));
        }
        return provider;
    }

    private synchronized void initProviders() {
        if (providerMap == null) {
            Map<String, ContextProvider> tmpMap = new HashMap<>();
            Map<String, ContextProvider> contextProviderBeans = OverrideBean.removeOverriddenBeans(applicationContext.getBeansOfType(ContextProvider.class));
            for (ContextProvider contextProvider : contextProviderBeans.values()) {
                for (String key : contextProvider.getParams()) {
                    ContextProvider pre = tmpMap.put(key, contextProvider);
                    if (pre != null)
                        throw new ContextException(
                                String.format("key '%s' defined in more then one context-provider ('%s' and '%s')", key, pre.getClass().getName(), contextProvider.getClass().getName()));
                }
            }
            providerMap = tmpMap;
        }
    }

    private synchronized void initPersistentProviders() {
        if (persistentProviderMap == null) {
            Map<String, PersistentContextProvider> tmpMap = new HashMap<>();
            for (PersistentContextProvider contextProvider : applicationContext.getBeansOfType(PersistentContextProvider.class).values()) {
                for (String key : contextProvider.getParams()) {
                    ContextProvider pre = tmpMap.put(key, contextProvider);
                    if (pre != null)
                        throw new ContextException(
                                String.format("key '%s' defined in more then one context-provider ('%s' and '%s')", key, pre.getClass().getName(), contextProvider.getClass().getName()));
                }
            }
            persistentProviderMap = tmpMap;
        }
    }

    /**
     * Проверка: кешируемый контекст или нет
     * @param param имя контекста
     * @return true - кешируемый, false - нет
     */
    public boolean isCachedParam(String param) {
        ContextProvider contextProvider = findProvider(param);
        return contextProvider.isCacheable();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
