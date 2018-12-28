package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;

/**
 * Базовый класс связывания любых метаданных
 * @param <D> Собранные метаданные
 */
public interface BaseMetadataBinder<D extends Compiled> extends MetadataBinder<D>, CompiledClassAware {
}
