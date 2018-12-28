package net.n2oapp.framework.api.metadata.global.aware;

/**
 * Знание об идентификаторе метаданной
 * @deprecated {@link net.n2oapp.framework.api.metadata.aware.IdAware}
 */
@Deprecated
public interface IdAware extends net.n2oapp.framework.api.metadata.aware.IdAware {
    String getId();

    @Override
    default void setId(String id) {
    }
}
