package net.n2oapp.framework.engine.sql;

import net.n2oapp.framework.engine.sql.rowmapper.PostgresIndexRowMapper;
import net.n2oapp.routing.datasource.JndiRoutingDataSourceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SqlConfiguration {
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
    @ConditionalOnClass(name="org.postgresql.util.PGobject")
    public PostgresIndexRowMapper postgresIndexRowMapper() {
        return new PostgresIndexRowMapper();
    }

//    @Bean
//    public SqlPageDao sqlPageDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
//        SqlPageDao sqlPageDao = new SqlPageDao();
//        sqlPageDao.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
//        return sqlPageDao;
//    }

}
