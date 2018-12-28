package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание о базовом типе сущности
 * @param <T> Базовый тип сущности
 */
public interface BaseElementClassAware<T> {
    Class<T> getBaseElementClass();
}
