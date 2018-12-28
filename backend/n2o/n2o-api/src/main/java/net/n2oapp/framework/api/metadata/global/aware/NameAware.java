package net.n2oapp.framework.api.metadata.global.aware;

/**
 * @deprecated {@link net.n2oapp.framework.api.metadata.aware.NameAware}
 */
@Deprecated
public interface NameAware extends net.n2oapp.framework.api.metadata.aware.NameAware {
    String getName();

    @Override
    default void setName(String name) {

    }
}
