package net.n2oapp.framework.context.smart.impl;

import java.util.Map;
import java.util.Set;

/**
 * @author rgalina
 * @since 17.05.2016
 */
public class PersistentProxyContext extends ProxyContext {
    public PersistentProxyContext(AbstractContextEngine contextEngine, Map<String, Object> baseParams) {
        super(contextEngine, baseParams);
    }

    public PersistentProxyContext(ProxyContext prevProxyContext, Set<String> params, Set<String> dependsOnParams) {
        super(prevProxyContext, params, dependsOnParams);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        Map<String, Object> executedDataSet = contextEngine.set(dataSet, this);
    }
}
