package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.util.CollectionUtils;

/**
 * Базовое связывание данных в регионах
 */
public abstract class BaseRegionBinder<D extends Region> implements BaseMetadataBinder<D> {

    @Override
    public D bind(D compiled, BindProcessor p) {
        if (!CollectionUtils.isEmpty(compiled.getContent()))
            compiled.getContent().forEach(p::bind);
        return compiled;
    }
}
