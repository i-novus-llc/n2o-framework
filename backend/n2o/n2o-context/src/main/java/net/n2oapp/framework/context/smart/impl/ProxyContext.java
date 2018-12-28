package net.n2oapp.framework.context.smart.impl;

import net.n2oapp.framework.api.context.ContextEngine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Прокси контекст, для определения графа зависимостей контекстов
 */
public class ProxyContext implements ContextEngine {

    protected AbstractContextEngine contextEngine;
    private Set<String> previousKeys;
    private Set<String> dependsOnParams;
    private Map<String, Object> baseParams;


    /**
     * Самая первая инициализация прокси контекста
     */
    public ProxyContext(AbstractContextEngine contextEngine, Map<String, Object> baseParams) {
        this.contextEngine = contextEngine;
        this.baseParams = baseParams;
    }

    /**
     * Инициализация прокси контекст на базе предыдущего
     */
    public ProxyContext(ProxyContext prevProxyContext, Set<String> params, Set<String> dependsOnParams) {
        this.contextEngine = prevProxyContext.getContextEngine();
        this.dependsOnParams = dependsOnParams;

        // Здесь специально одна и та же ссылка на base params.
        // Потому что этот создаваемый прокси может доложить в base только что полученные значения.
        // И этими значениями могут пользоваться prev прокси.
        this.baseParams = prevProxyContext.baseParams;

        if (dependsOnParams != null && !dependsOnParams.isEmpty()) {
            // Проверка рекурсивных связей
            this.previousKeys = new HashSet<>(prevProxyContext.getPreviousKeys());
            this.previousKeys.addAll(params);
            for (String key : dependsOnParams) {
                if (this.previousKeys.contains(key))
                    throw new ContextRecursiveException(key);
            }
        }
    }

    public boolean isBaseParam(String param) {
        return baseParams != null && baseParams.containsKey(param);
    }

    public AbstractContextEngine getContextEngine() {
        return contextEngine;
    }

    public Set<String> getPreviousKeys() {
        return previousKeys != null ? previousKeys : Collections.<String>emptySet();
    }

    @Override
    public Object get(String key) {
        // Проверка на несоответствие декларации dependsOn и использования ctx.get(...)
//        if (dependsOnParams != null && !dependsOnParams.canResolved(key)) {
//            throw new ChangeDependencyException(key);
//        } //todo в этой проверке есть баг, поэтому убрал её
        // Получение параметра из ранее известных значений в графе контекстов
        if (baseParams != null && baseParams.containsKey(key))
            return baseParams.get(key);
        Object result = contextEngine.get(key, this);

        // Вставляем в base params то, что только что получили из кеша или провайдера.
        // Это делается на тот случай, если этот контекст запросили дети в своем провайдере.
        // Детям понадобится вычислить ключ, состоящий из значений родителей.
        // Во время получения ключа в кеш родителей уже не полезем, а возьмём из base params, которые предусмотрительно сейчас кладем.
        baseParams.put(key, result);

        return result;
    }

    /**
     * Проверка: кешируемый контекст или нет
     * @param param имя контекста
     * @return true - кешируемый, false - нет
     */
    public boolean isCachedParam(String param) {
        return contextEngine.isCachedParam(param);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object get(String param, Map<String, Object> baseParams) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(Map<String, Object> dataSet, Map<String, Object> baseParams) {
        throw new UnsupportedOperationException();
    }

}
