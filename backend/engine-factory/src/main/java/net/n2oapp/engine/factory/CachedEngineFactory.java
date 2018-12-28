package net.n2oapp.engine.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Фабрика движков, кэширующая свою продукцию
 */
public abstract class CachedEngineFactory<T, G> implements EngineFactory<T, G> {
    private volatile Map<T, G> engines;

    @Override
    public G produce(T type) {
        G engine = getEngine(type);
        if (engine == null)
            throw new EngineNotFoundException(type);
        return engine;
    }


    private synchronized void initFactory() {
        if (engines == null) {
            Collection<G> engineBeans = findEngines();
            Map<T, G> result = new HashMap<>();
            for (G engine : engineBeans) {
                result.put(getType(engine), engine);
            }
            engines = result;
        }
    }

    protected G getEngine(T type) {
        if (engines == null)
            initFactory();
        return engines.get(type);
    }

    public abstract Collection<G> findEngines();

    public abstract T getType(G engine);
}
