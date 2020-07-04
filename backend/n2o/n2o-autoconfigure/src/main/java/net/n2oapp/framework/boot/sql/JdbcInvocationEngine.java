package net.n2oapp.framework.boot.sql;

import net.n2oapp.framework.api.data.ActionInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.DataSourceAware;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.routing.datasource.JndiRoutingDataSourceTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Абстрактный сервис выполнения jdbc запросов
 */
@Deprecated
public abstract class JdbcInvocationEngine<T extends N2oInvocation & DataSourceAware> implements ActionInvocationEngine<T> {
    private JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcInvocationEngine(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public JdbcInvocationEngine(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate) {
        this.jndiRoutingDataSourceTemplate = jndiRoutingDataSourceTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    protected abstract List<String> getQueries(T invocation);

    protected Object[] execute(String jndiName, final List<String> queries, final Map<String, Object> args) {
        if (jndiRoutingDataSourceTemplate != null)
            return jndiRoutingDataSourceTemplate.execute(jndiName, transactionStatus -> executeSql(queries, args));
        else
            return executeSql(queries, args);
    }

    private Object[] executeSql(List<String> queries, Map<String, Object> args) {
        try {
            return SqlJpqlUtil.executeQueries(namedParameterJdbcTemplate, queries, args);
        } catch (N2oException e) {
            String summary = InvocationUtil.findSqlSummary(e);
            if (summary != null)
                throw new N2oUserException(summary);
            throw e;
        }
    }

    public JndiRoutingDataSourceTemplate getJndiRoutingDataSourceTemplate() {
        return jndiRoutingDataSourceTemplate;
    }

    public void setJndiRoutingDataSourceTemplate(JndiRoutingDataSourceTemplate jndiRoutingDataSourceTemplate) {
        this.jndiRoutingDataSourceTemplate = jndiRoutingDataSourceTemplate;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

}
