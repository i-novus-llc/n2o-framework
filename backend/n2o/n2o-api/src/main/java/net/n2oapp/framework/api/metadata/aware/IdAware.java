package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание об идентификаторе метаданной
 */
public interface IdAware {
    String getId();

    default void setId(String id) {
    }
}
