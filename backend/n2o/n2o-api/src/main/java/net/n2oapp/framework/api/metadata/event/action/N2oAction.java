package net.n2oapp.framework.api.metadata.event.action;


import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.SourceAware;

/**
 * Сурс модель эвента
 */
public interface N2oAction extends Source, SourceAware, IdAware, ClientMetadata, NamespaceUriAware {

    @Override
    String getId();

    void setId(String id);

    @Override
    default boolean isProcessable() {
        return true;
    }

    @Override
    default String getNamespaceUri() {
        return "";
    }

    @Deprecated
    default String getOperationId() { return null; }
    default String getObjectId() { return null; }
}
