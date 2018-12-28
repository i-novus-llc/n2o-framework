package net.n2oapp.framework.api.metadata.global.dao.execution.base;

/**
 * User: iryabov
 * Date: 28.10.13
 * Time: 10:11
 */
public interface DataSourceAware {
    String getDataSource();

    void setDataSource(String dataSource);
}
