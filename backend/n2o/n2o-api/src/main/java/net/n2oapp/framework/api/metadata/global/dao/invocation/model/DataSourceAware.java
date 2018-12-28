package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

/**
 * @author V. Alexeev.
 */
public interface DataSourceAware {

    String getDataSource();

    void setDataSource(String dataSource);

}
