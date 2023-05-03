package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание об идентификаторе источника данных
 */
public interface DatasourceIdAware {
    String getDatasourceId();

    void setDatasourceId(String datasourceId);
}
