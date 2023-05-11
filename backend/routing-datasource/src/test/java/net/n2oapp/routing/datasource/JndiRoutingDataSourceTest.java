package net.n2oapp.routing.datasource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dednakov on 13.12.2016.
 */
public class JndiRoutingDataSourceTest {

    @Test
    public void testGetConnection() throws Exception {
        JndiRoutingDataSource jndiRoutingDataSource = new JndiRoutingDataSource();
        try {
            Connection connection = jndiRoutingDataSource.getConnection();
            fail();
        } catch (IllegalStateException e) {

        }

//        Test: determineTargetDataSource(): dataSource = resolveDataSourceByLookupKey(key)
        DataSourceLookup dataSourceLookup = mock(DataSourceLookup.class);
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(dataSourceLookup.getDataSource(eq("test"))).thenReturn(dataSource);
        jndiRoutingDataSource = new JndiRoutingDataSource();
        jndiRoutingDataSource.setLazyDetermineDS(true);
        jndiRoutingDataSource.setDataSourceLookup(dataSourceLookup);
        jndiRoutingDataSource.setDefaultLookupKey("test");
        Connection connection1 = jndiRoutingDataSource.getConnection();
        assertSame(connection, connection1);

//        Test: determineTargetDataSource(): dataSource = resolveDataSourceByLookupKey(key), dataSource instanceof String
        jndiRoutingDataSource.addDataSource("test");
        Connection connection4 = jndiRoutingDataSource.getConnection();
        assertSame(connection, connection4);

//        Test: determineTargetDataSource(): dataSource = defaultTargetDataSource
        DataSource defaultTargetDataSource = mock(DataSource.class);
        when(defaultTargetDataSource.getConnection()).thenReturn(connection);
        jndiRoutingDataSource = new JndiRoutingDataSource();
        jndiRoutingDataSource.setLazyDetermineDS(false);
        jndiRoutingDataSource.setDefaultLookupKey("test");
        jndiRoutingDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
        Connection connection2 = jndiRoutingDataSource.getConnection();
        assertSame(connection, connection2);

//        Test: determineTargetDataSource(): dataSource = defaultTargetDataSource, getConnection(String str1, String str2)
        Connection connectionWithParameters = mock(Connection.class);
        when(defaultTargetDataSource.getConnection("test", "test")).thenReturn(connectionWithParameters);
        Connection connection3 = jndiRoutingDataSource.getConnection("test", "test");
        assertSame(connectionWithParameters, connection3);
    }

    @Test
    public void testAddDataSource() throws Exception {
        JndiRoutingDataSource jndiRoutingDataSource = new JndiRoutingDataSource();
        DataSourceLookup dataSourceLookup = mock(DataSourceLookup.class);
        when(dataSourceLookup.getDataSource("test")).thenReturn(mock(DataSource.class));
        jndiRoutingDataSource.setLazyDetermineDS(true);
        jndiRoutingDataSource.setDataSourceLookup(dataSourceLookup);
        jndiRoutingDataSource.addDataSource("test");
        DataSource dataSource1 = dataSourceLookup.getDataSource("test");
        jndiRoutingDataSource.addDataSource("test");
        DataSource dataSource2 = dataSourceLookup.getDataSource("test");
        assertSame(dataSource1, dataSource2);
    }
}
