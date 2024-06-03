package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.sql.SqlDataProviderEngine;
import net.n2oapp.framework.boot.sql.rowmapper.IndexRowMapper;
import net.n2oapp.framework.boot.sql.rowmapper.MapRowMapper;
import net.n2oapp.framework.boot.sql.rowmapper.PostgresIndexRowMapper;
import net.n2oapp.framework.boot.sql.rowmapper.PostgresMapRowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@AutoConfiguration
@ConditionalOnClass(JdbcTemplate.class)
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class N2oSqlAutoConfiguration {

    @Value("${n2o.engine.sql.driver-class-name:org.h2.Driver}")
    private String defaultJdbcDriver;

    @Bean
    @ConditionalOnMissingBean
    public SqlDataProviderEngine sqlDataProviderEngine() {
        SqlDataProviderEngine sqlDataProviderEngine = new SqlDataProviderEngine();
        sqlDataProviderEngine.setDefaultJdbcDriver(defaultJdbcDriver);
        return sqlDataProviderEngine;
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
