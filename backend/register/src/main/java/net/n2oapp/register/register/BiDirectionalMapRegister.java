package net.n2oapp.register.register;

import net.n2oapp.register.extractor.Extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: iryabov
 * Date: 15.02.13
 * Time: 11:43
 */
public class BiDirectionalMapRegister<S, V> implements Register<S, V> {
    protected Extractor<S, V> extractor;
    protected Map<S, V> registerMap = createInitialRegisterMap();
    protected Map<V, S> inverseMap = createInverseRegisterMap();

    protected Map<S, V> createInitialRegisterMap() {
        return new HashMap<S, V>();
    }

    protected Map<V, S> createInverseRegisterMap() {
        return new HashMap<V, S>();
    }

    @Override
    public synchronized V add(S source) {
        if (!extractor.isExtractable(source)) return null;
        V value = extractor.extract(source);
        removeIfContains(source, value);
        registerMap.put(source, value);
        inverseMap.put(value, source);
        return value;
    }

    private void removeIfContains(S source, V value) {
        if (registerMap.containsKey(source)) {
            inverseMap.remove(registerMap.get(source));
            registerMap.remove(source);

        }
        if (inverseMap.containsKey(value)) {
            registerMap.remove(inverseMap.get(value));
            inverseMap.remove(value);
        }
    }

    @Override
    public synchronized void clear() {
        registerMap.clear();
        inverseMap.clear();
    }

    @Override
    public V getValue(S source) {
        return registerMap.get(source);
    }

    @Override
    public S getSource(V value) {
        return inverseMap.get(value);
    }

    @Override
    public Set<V> values() {
        return inverseMap.keySet();
    }

    @Override
    public Set<S> sources() {
        return registerMap.keySet();
    }

    @Deprecated
    protected void initializeRegister() {
        registerMap = new HashMap<S, V>();
        inverseMap = new HashMap<V, S>();
    }

    public synchronized void addAll(Map<S, V> map) {
        registerMap.putAll(map);
        inverse(map);
    }

    private void inverse(Map<S, V> map) {
        for (Map.Entry<S, V> entry : map.entrySet()) {
            inverseMap.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void setExtractor(Extractor<S, V> extractor) {
        this.extractor = extractor;
    }
}