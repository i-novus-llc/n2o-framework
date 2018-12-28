package net.n2oapp.routing.datasource;

import javax.sql.DataSource;

/**
 * User: iryabov
 * Date: 26.08.13
 * Time: 12:18
 */
public class JndiRoutingDataSource extends RuntimeRoutingDataSource<String> {
    public void addDataSource(String jndiName) {
        super.addDataSource(jndiName, jndiName);
    }

    @Override
    protected String determineCurrentLookupKey() {
        return JndiContextHolder.getJndiName();
    }

    @Override
    protected DataSource resolveDataSourceByLookupKey(String lookupKey) {
        return resolveSpecifiedDataSource(lookupKey);
    }
}
