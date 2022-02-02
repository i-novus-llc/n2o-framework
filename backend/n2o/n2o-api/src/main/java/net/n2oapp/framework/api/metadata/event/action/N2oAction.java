package net.n2oapp.framework.api.metadata.event.action;


import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

/**
 * Сурс модель эвента
 */
public interface N2oAction extends Source, SrcAware, IdAware, NamespaceUriAware, ExtensionAttributesAware {

    @Override
    String getId();

    void setId(String id);

    @Override
    default String getNamespaceUri() {
        return "";
    }

    @Deprecated
    default String getOperationId() { return null; }
    @Deprecated
    default String getObjectId() { return null; }
}
