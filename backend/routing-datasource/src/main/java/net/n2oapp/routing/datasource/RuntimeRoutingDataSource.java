package net.n2oapp.routing.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Динамический источник данных с маршрутизатором.
 * Основано на {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource}
 */
public abstract class RuntimeRoutingDataSource<T> extends AbstractDataSource {
    private DataSource defaultTargetDataSource;
    private T defaultLookupKey;

    private boolean lazyDetermineDS = false;

    private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();

    private Map<T, DataSource> resolvedDataSources = new ConcurrentHashMap<T, DataSource>();

    public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    public void setDefaultLookupKey(T defaultLookupKey) {
        this.defaultLookupKey = defaultLookupKey;
    }

    public void setLazyDetermineDS(boolean lazyDetermineDS) {
        this.lazyDetermineDS = lazyDetermineDS;
    }

    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
        this.dataSourceLookup = (dataSourceLookup != null ? dataSourceLookup : new JndiDataSourceLookup());
    }

    public void addDataSource(T lookupKey, Object dataSource) {
        if (resolvedDataSources.containsKey(lookupKey)) {
            return;
        }
        DataSource specifiedDataSource = resolveSpecifiedDataSource(dataSource);
        this.resolvedDataSources.put(lookupKey, specifiedDataSource);
    }

    protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        if (dataSource instanceof DataSource) {
            return (DataSource) dataSource;
        } else if (dataSource instanceof String) {
            return this.dataSourceLookup.getDataSource((String) dataSource);
        } else {
            throw new IllegalArgumentException(
                    "Illegal data source value - only [javax.sql.DataSource] and String supported: " + dataSource);
        }
    }


    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    protected DataSource determineTargetDataSource() {
        T lookupKey = determineCurrentLookupKey();
        T key = lookupKey != null ? lookupKey : defaultLookupKey;
        DataSource dataSource = key != null ? this.resolvedDataSources.get(key) : null;
        if(dataSource == null) {
            if (key != null && key.equals(defaultLookupKey) && defaultTargetDataSource != null) {
                dataSource = defaultTargetDataSource;
            } else if (key != null && lazyDetermineDS) {
                dataSource = resolveDataSourceByLookupKey(key);
            } else {
                throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
            }
        }
        return dataSource;
    }

    protected abstract T determineCurrentLookupKey();

    protected abstract DataSource resolveDataSourceByLookupKey(T lookupKey);
}
