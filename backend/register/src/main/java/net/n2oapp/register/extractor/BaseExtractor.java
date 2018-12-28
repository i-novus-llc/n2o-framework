package net.n2oapp.register.extractor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * User: IRyabov
 * Date: 25.01.12
 * Time: 11:51
 */
public abstract class BaseExtractor<S, E> implements Extractor<S, E> {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<S, E> extractMap(Collection<S> sources) {
        Map<S, E> entities = new LinkedHashMap<S, E>();
        for (S source : sources) {
            try {
                E entity = extract(source);
                if (entity != null) {
                    entities.put(source, entity);
                }
            } catch (Exception e) {
                log.error("Extract failed", e);
            }
        }
        return entities;
    }

    @Override
    public Collection<E> extract(Collection<S> sources) {
        return extractMap(sources).values();
    }

    @Override
    public boolean isExtractable(S source) {
        return true;
    }

}
