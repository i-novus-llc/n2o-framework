package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание о ссылке на метаданную
 */
public interface RefIdAware {

    String getRefId();

    void setRefId(String refId);
}
