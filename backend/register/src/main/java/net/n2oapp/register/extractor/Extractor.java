package net.n2oapp.register.extractor;

import java.util.Collection;
import java.util.Map;

/**
 * User: IRyabov
 * Date: 25.01.12
 * Time: 11:26
 */
public interface Extractor<S, V> {
    Collection<V> extract(Collection<S> sources);

    V extract(S source);

    Map<S, V> extractMap(Collection<S> sources);

    boolean isExtractable(S source);
}
