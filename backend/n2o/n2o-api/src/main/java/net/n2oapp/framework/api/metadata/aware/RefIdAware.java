package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.SourceMetadata;

/**
 * Знание о ссылке на метаданную
 */
public interface RefIdAware extends SourceMetadata {

    String getRefId();

    void setRefId(String refId);
}
