package net.n2oapp.framework.api.metadata.global;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;

/**
 * Базовый класс исходных метаданных считанных из файла
 */
public abstract class N2oMetadata implements SourceMetadata, IdAware, RefIdAware, NameAware, SourceComponent {
    private String namespaceUri;
    private String id;
    private String refId;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRefId() {
        return refId;
    }

    @Override
    public void setRefId(String refId) {
        this.refId = refId;
    }

}
