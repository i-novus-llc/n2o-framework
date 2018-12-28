package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

/**
 * Вызов sql выражения
 */
@Deprecated
public class N2oSqlQuery implements N2oMapInvocation, DataSourceAware {

    private String query;
    @Deprecated
    private String dataSource;
    private String namespaceUri;
    @Deprecated
    private String countQuery;

    public String getCountQuery() {
        return countQuery;
    }

    public void setCountQuery(String countQuery) {
        this.countQuery = countQuery;
    }

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
