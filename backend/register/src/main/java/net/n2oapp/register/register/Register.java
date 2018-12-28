package net.n2oapp.register.register;


import net.n2oapp.register.extractor.Extractor;

import java.util.Set;

/**
 * User: IRyabov
 * Date: 25.01.12
 * Time: 12:02
 */
public interface Register<S, V> {
    V add(S source);

    void clear();

    V getValue(S source);

    S getSource(V value);

    Set<V> values();

    Set<S> sources();

    void setExtractor(Extractor<S, V> extractor);
}
