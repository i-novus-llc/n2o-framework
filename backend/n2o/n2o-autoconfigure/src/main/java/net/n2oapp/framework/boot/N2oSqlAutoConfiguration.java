package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.sql.SqlDataProviderEngine;
import net.n2oapp.framework.boot.sql.SqlInvocationEngine;
import net.n2oapp.framework.boot.sql.rowmapper.IndexRowMapper;
import net.n2oapp.framework.boot.sql.rowmapper.MapRowMapper;
import net.n2oapp.framework.boot.sql.rowmapper.PostgresIndexRowMapper;
import net.n2oapp.framework.boot.sql.rowmapper.PostgresMapRowMapper;
import net.n2oapp.routing.datasource.JndiRoutingDataSourceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@ConditionalOnClass(JdbcTemplate.class)
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class N2oSqlAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SqlInvocationEngine sqlInvocationEngine(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                                   @Autowired(required = false) JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate) {
        return new SqlInvocationEngine(namedParameterJdbcTemplate, jndiRoutingDataSourceTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlDataProviderEngine sqlDataProviderEngine() {
        return new SqlDataProviderEngine();
    }

    @Bean
    public IndexRowMapper indexRowMapper() {
        return new IndexRowMapper();
    }

    @Bean
    public MapRowMapper mapRowMapper() {
        return new MapRowMapper();
    }

    @Bean
    @ConditionalOnClass(name = "org.postgresql.util.PGobject")
    public PostgresIndexRowMapper postgresIndexRowMapper() {
        return new PostgresIndexRowMapper();
    }

    @Bean
    @ConditionalOnClass(name = "org.postgresql.util.PGobject")
    public PostgresMapRowMapper postgresMapRowMapper() {
        return new PostgresMapRowMapper();
    }
}
