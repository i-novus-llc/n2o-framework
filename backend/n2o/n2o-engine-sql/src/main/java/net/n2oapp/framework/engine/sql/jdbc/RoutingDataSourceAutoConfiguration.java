package net.n2oapp.framework.engine.sql.jdbc;

import net.n2oapp.routing.datasource.JndiRoutingDataSource;
import net.n2oapp.routing.datasource.JndiRoutingDataSourceTemplate;
import net.n2oapp.routing.datasource.RuntimeRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Авто конфигурация динамического источника данных с маршрутизатором
 */
@Configuration
@ConditionalOnClass({RuntimeRoutingDataSource.class})
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter({JndiDataSourceAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
public class RoutingDataSourceAutoConfiguration {

    private final DataSource defaultDataSource;
    @Value("${n2o.engine.jdbc.datasource}")
    private String jdbcName;
    @Value("${n2o.engine.timeout}")
    private int timeout;


    public RoutingDataSourceAutoConfiguration(@Qualifier("dataSource") DataSource dataSource) {
        this.defaultDataSource = dataSource;
    }

    @Bean
    @Primary
    @DependsOn("dataSourceInitializer")
    @ConditionalOnMissingBean(JndiRoutingDataSource.class)
    public JndiRoutingDataSource routingDataSource(ApplicationContext applicationContext) {
        JndiRoutingDataSource routingDataSource = new JndiRoutingDataSource();
        Map<String, RoutingDataSourceInitializer> initializers = applicationContext.getBeansOfType(RoutingDataSourceInitializer.class);
        routingDataSource.setLazyDetermineDS(true);
        routingDataSource.setDefaultLookupKey(this.jdbcName);
        routingDataSource.setDefaultTargetDataSource(this.defaultDataSource);
        for (RoutingDataSourceInitializer initializer : initializers.values()) {
            initializer.initialize(routingDataSource);
        }
        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager routingTransactionManager(JndiRoutingDataSource routingDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(routingDataSource);
        transactionManager.setDefaultTimeout(this.timeout);
        return transactionManager;
    }


    @Bean
    @Primary
    public TransactionTemplate routingTransactionTemplate(DataSourceTransactionManager routingTransactionManager) {
        return new TransactionTemplate(routingTransactionManager);
    }

    @Bean
    @Primary
    public JdbcTemplate routingJdbcTemplate(JndiRoutingDataSource routingDataSource) {
        return new JdbcTemplate(routingDataSource);
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate routingNamedParameterJdbcTemplate(JndiRoutingDataSource routingDataSource) {
        return new NamedParameterJdbcTemplate(routingDataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate(TransactionTemplate routingTransactionTemplate) {
        JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate = new JndiRoutingDataSourceTemplate();
        jndiRoutingDataSourceTemplate.setTransactionTemplate(routingTransactionTemplate);
        return jndiRoutingDataSourceTemplate;
    }


    public void setJdbcName(String jdbcName) {
        this.jdbcName = jdbcName;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
