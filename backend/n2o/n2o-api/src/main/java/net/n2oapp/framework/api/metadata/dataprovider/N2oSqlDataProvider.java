package net.n2oapp.framework.api.metadata.dataprovider;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

/**
 *  SQL провайдер данных
 */
public class N2oSqlDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String query;
    private String filePath;
    private String dataSource;
    private String rowMapper;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(String rowMapper) {
        this.rowMapper = rowMapper;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
